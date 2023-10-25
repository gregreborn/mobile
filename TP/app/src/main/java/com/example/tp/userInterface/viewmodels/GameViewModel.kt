package com.example.tp.userInterface.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tp.data.model.Card
import com.example.tp.data.repository.GameRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import com.example.tp.data.model.DeckResponse
import com.example.tp.data.model.DrawCardResponse
import com.example.tp.utils.GameLogic
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GameViewModel(private val repository: GameRepository) : ViewModel() {

    // LiveData for deck and drawn card
    private val _deckData: MutableLiveData<DeckResponse?> = MutableLiveData()
    val deckData: LiveData<DeckResponse?> = _deckData

    private val _drawnCardData: MutableLiveData<DrawCardResponse?> = MutableLiveData()
    val drawnCardData: LiveData<DrawCardResponse?> = _drawnCardData

    private val _playerCards: MutableLiveData<List<Card>> = MutableLiveData(emptyList())
    val playerCards: LiveData<List<Card>> = _playerCards

    private val _dealerCards: MutableLiveData<List<Card>> = MutableLiveData(emptyList())
    val dealerCards: LiveData<List<Card>> = _dealerCards

    private val _playerChips: MutableLiveData<Int> = MutableLiveData(100)
    val playerChips: LiveData<Int> = _playerChips

    private val _currentBet: MutableLiveData<Int> = MutableLiveData(0)
    val currentBet: LiveData<Int> = _currentBet

    private val _splitOffered: MutableLiveData<Boolean> = MutableLiveData(false)
    val splitOffered: LiveData<Boolean> = _splitOffered

    private val _playerSplitHands: MutableLiveData<List<List<Card>>> = MutableLiveData(emptyList())
    val playerSplitHands: LiveData<List<List<Card>>> = _playerSplitHands

    private val _activeHandIndex: MutableLiveData<Int> = MutableLiveData(-1)
    val activeHandIndex: LiveData<Int> = _activeHandIndex

    //------------------------------------------------------------------------------------------//

    init {
        Log.d("GameViewModel", "Initializing game...")
        Log.d("GameViewModel", "Initial playerCards: ${_playerCards.value}")
        Log.d("GameViewModel", "Initial dealerCards: ${_dealerCards.value}")

        initializeGame()
    }

    fun handleSplit() {
        Log.d("GameViewModel", "Handling split...")
        val currentCards = _playerCards.value ?: emptyList()
        if (currentCards.size == 2 && currentCards[0].value == currentCards[1].value) {
            val splitHands = listOf(listOf(currentCards[0]), listOf(currentCards[1]))
            _playerSplitHands.value = splitHands
            _playerCards.value = emptyList() // Clear original player cards.
            _activeHandIndex.value = 0 // Set first split hand as active.
            Log.d("GameViewModel", "After Split: playerSplitHands: ${_playerSplitHands.value}, playerCards: ${_playerCards.value}")
        }
    }


    fun standForSplitHand() {
        _activeHandIndex.value = (_activeHandIndex.value ?: 0) + 1
        if (_activeHandIndex.value!! >= _playerSplitHands.value?.size ?: 0) {
            // All split hands have been acted upon, move to the dealer's turn.
            _activeHandIndex.value = -1 // Reset active hand index.
            stand()
        }
    }


    fun fetchNewDeck() {
        Log.d("GameViewModel", "Fetching new deck...")

        viewModelScope.launch {
            val response: Response<DeckResponse> = repository.fetchNewDeck()
            if (response.isSuccessful) {
                Log.d("GameViewModel", "Successfully fetched new deck.")

                _deckData.value = response.body()
            } else {
                Log.e("GameViewModel", "Failed to fetch new deck. Response code: ${response.code()}")
                _deckData.value = null
            }
        }
    }

    fun initializeGame() {
        fetchNewDeck()
        // TODO: Reset any other game state if needed
    }

    private fun updateChipsAfterRound(result: String) {
        when (result) {
            "Player Wins!" -> _playerChips.value = (_playerChips.value ?: 0) + _currentBet.value!! * 2
            "Dealer Wins!" -> {}  // Player has already lost their bet
            "It's a Tie!" -> _playerChips.value = (_playerChips.value ?: 0) + _currentBet.value!!
        }
        _currentBet.value = 0
    }

    suspend fun drawCard(deckId: String): Card? {
        Log.d("GameViewModel", "Drawing card...")
        val response = repository.drawCard(deckId)
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.cards.isNullOrEmpty()) {
                    Log.e("GameViewModel", "Received empty cards list. Response: $it")
                    return null
                }
                return it.cards[0]
            }
        } else {
            Log.e("GameViewModel", "Failed to draw card. Error: ${response.errorBody()?.string()}")
        }
        return null
    }

    fun drawCardForPlayer(deckId: String) {
        Log.d("GameViewModel", "Drawing card for player...")
        viewModelScope.launch {
            val card = drawCard(deckId) ?: return@launch

            if (_activeHandIndex.value != null && _activeHandIndex.value != -1) { // Active split hand.
                val updatedHands = _playerSplitHands.value?.toMutableList() ?: mutableListOf()
                if (_activeHandIndex.value!! < updatedHands.size) {
                    val currentHand = updatedHands[_activeHandIndex.value!!].toMutableList()
                    currentHand.add(card)
                    updatedHands[_activeHandIndex.value!!] = currentHand
                    _playerSplitHands.value = updatedHands
                } else {
                    // Handle error case: unexpected active hand index.
                }
            } else { // No active split hand.
                val updatedCards = _playerCards.value?.toMutableList() ?: mutableListOf()
                updatedCards.add(card)
                _playerCards.value = updatedCards
                checkForSplitOption() // Check for split option only for the first two cards.
            }

            val cardsToCheck = if (_activeHandIndex.value != null && _activeHandIndex.value != -1 && _playerSplitHands.value?.isNotEmpty() == true) {
                _playerSplitHands.value?.get(_activeHandIndex.value!!)
            } else {
                _playerCards.value
            } ?: emptyList()
            if (GameLogic.isBlackjack(cardsToCheck)) {
                _gameResult.value = "Player Wins with Blackjack!"
            } else if (GameLogic.isBust(cardsToCheck)) {
                _gameResult.value = "Player Busted! Dealer Wins!"
            }
        }
    }

    fun placeBetAsync(betAmount: Int) {
        viewModelScope.launch {
            placeBet(betAmount)
        }
    }


    suspend fun placeBet(betAmount: Int) {
        Log.d("GameViewModel", "Placing bet: $betAmount")
        if (betAmount in 1..(_playerChips.value ?: 0)) {
            _currentBet.value = betAmount
            _playerChips.value = (_playerChips.value ?: 0) - betAmount

            // After placing a valid bet, deal cards
            dealInitialCards(_deckData.value?.deck_id ?: return)

        } else {
            // Handle invalid bet, maybe show a message
            _gameResult.value = "Invalid Bet Amount!"
        }
    }

    private suspend fun dealInitialCards(deckId: String) {
        // Draw two cards for the player and two cards for the dealer
        withContext(Dispatchers.IO) {
            drawCardForPlayer(deckId)
            drawInitialCardForDealer(deckId)
            drawCardForPlayer(deckId)
            drawInitialCardForDealer(deckId)
        }

        Log.d("GameViewModel", "After Initial Deal: playerCards: ${_playerCards.value}")
        Log.d("GameViewModel", "After Initial Deal: dealerCards: ${_dealerCards.value}")
    }

    private fun checkForSplitOption() {
        if (_playerCards.value?.size == 2 && _playerCards.value!![0].value == _playerCards.value!![1].value) {
            _splitOffered.value = true
        }
    }

    fun resetGameState() {
        Log.d("GameViewModel", "Resetting game state...")
        _playerCards.value = emptyList()
        _dealerCards.value = emptyList()
        _currentBet.value = 0
        _splitOffered.value = false
        _gameResult.value = ""
        // You might also want to fetch a new deck here or as part of the game's flow
        fetchNewDeck()
    }


    fun stand() {
        Log.d("GameViewModel", "Player stands.")
        // If the player has opted for split hands and is yet to act on all of them
        if (_activeHandIndex.value ?: -1 >= 0) {
            standForSplitHand()
            return
        }

        // If no active split hand or all split hands acted upon, proceed with dealer's turn
        // First, reveal the dealer's hidden card
        drawInitialCardForDealer(_deckData.value?.deck_id ?: return)
    }




    //-------------------------------------------Dealer's actions-------------//

    private val _gameResult = MutableLiveData<String>()
    val gameResult: LiveData<String> = _gameResult


    fun drawInitialCardForDealer(deckId: String) {
        Log.d("GameViewModel", "Drawing initial card for dealer...")
        viewModelScope.launch {
            val card = drawCard(deckId) ?: return@launch
            val updatedCards = _dealerCards.value?.toMutableList() ?: mutableListOf()
            updatedCards.add(card)
            _dealerCards.value = updatedCards

            Log.d("GameViewModel", "Updated dealerCards: ${_dealerCards.value}")
        }
    }

    fun continueDealerTurn(deckId: String) {
        Log.d("GameViewModel", "Continuing dealer's turn...")
        viewModelScope.launch {
            val card = drawCard(deckId) ?: return@launch
            val updatedCards = _dealerCards.value?.toMutableList() ?: mutableListOf()
            updatedCards.add(card)
            _dealerCards.value = updatedCards
            Log.d("GameViewModel", "Updated dealerCards: ${_dealerCards.value}")

            if (GameLogic.calculateSum(updatedCards) <= 16) {
                continueDealerTurn(deckId)
            } else {
                // Dealer's turn ends, now decide the winner
                decideWinner()
            }
        }
    }


    private fun decideWinner() {
        Log.d("GameViewModel", "Deciding winner...")
        val playerSum = GameLogic.calculateSum(_playerCards.value ?: listOf())
        val dealerSum = GameLogic.calculateSum(_dealerCards.value ?: listOf())

        when {
            GameLogic.isBust(_playerCards.value ?: listOf()) -> {
                // Player busted, dealer wins
                _gameResult.value = "Dealer Wins!"
            }
            GameLogic.isBust(_dealerCards.value ?: listOf()) -> {
                // Dealer busted, player wins
                _gameResult.value = "Player Wins!"
            }
            playerSum > dealerSum -> {
                // Player has a higher score, player wins
                _gameResult.value = "Player Wins!"
            }
            dealerSum > playerSum -> {
                // Dealer has a higher score, dealer wins
                _gameResult.value = "Dealer Wins!"
            }
            else -> {
                // It's a tie
                _gameResult.value = "It's a Tie!"
            }
        }
        updateChipsAfterRound(_gameResult.value!!)
    }


}

class GameViewModelFactory(private val repository: GameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

