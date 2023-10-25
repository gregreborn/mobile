package com.example.tp.userInterface.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp.data.repository.GameRepository
import kotlinx.coroutines.launch
import com.example.tp.data.db.entities.Statistics

class StatisticsViewModel(private val repository: GameRepository) : ViewModel() {

    // LiveData for statistics data
    private val _statisticsData: MutableLiveData<List<Statistics>?> = MutableLiveData()
    val statisticsData: LiveData<List<Statistics>?> = _statisticsData
    private val totalCards = 364  // total number of cards in the 7 decks at the start
    private val initialCardCounts = mapOf(
        "Ace" to 28,
        "10" to 112,
        "9" to 28,
        "8" to 28,
        "7" to 28,
        "6" to 28,
        "5" to 28,
        "4" to 28,
        "3" to 28,
        "2" to 28
    )
    //_cardCounts keeps track of the current count of each card type.
    private val _cardCounts = MutableLiveData(initialCardCounts)
    val cardCounts: LiveData<Map<String, Int>> = _cardCounts

    //_cardProbabilities stores the probability for each card type.
    private val _cardProbabilities = MutableLiveData<Map<String, Float>>()
    val cardProbabilities: LiveData<Map<String, Float>> = _cardProbabilities


    //trackCard decreases the count of a particular card type when it's drawn.
    fun trackCard(cardType: String) {
        val currentCount = _cardCounts.value?.get(cardType) ?: 0
        _cardCounts.value = _cardCounts.value?.plus(Pair(cardType, currentCount - 1))
        updateProbabilities()
    }

    //updateProbabilities recalculates the probabilities after a card is drawn.
    private fun updateProbabilities() {
        val updatedProbabilities = mutableMapOf<String, Float>()
        _cardCounts.value?.forEach { (cardType, count) ->
            updatedProbabilities[cardType] = (count.toFloat() / totalCards) * 100
        }
        _cardProbabilities.value = updatedProbabilities
    }

    //resetStatistics reinitializes the counts and probabilities to their original state.
    fun resetStatistics() {
        _cardCounts.value = initialCardCounts
        updateProbabilities()
    }

    // Retrieve statistics data from DB (If necessary)
    fun fetchStoredStatistics() {
        viewModelScope.launch {
            val statistics = repository.getAllStatistics()
            // Process and update LiveData
        }
    }
    // TODO: Add more functions if needed.
}

