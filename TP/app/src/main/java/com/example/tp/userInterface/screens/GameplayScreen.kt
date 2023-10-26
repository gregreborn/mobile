package com.example.tp.userInterface.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tp.userInterface.viewmodels.GameViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp.userInterface.components.BettingComponent
import com.example.tp.userInterface.components.CardComponent
import androidx.compose.material3.Card
import com.example.tp.userInterface.components.DisplayHand
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@Composable
fun GameplayScreen(navController: NavController, viewModel: GameViewModel) {


    // Observe ViewModel LiveData/State
    val playerCards by viewModel.playerCards.observeAsState(initial = emptyList())
    val dealerCards by viewModel.dealerCards.observeAsState(initial = emptyList())
    val gameResult by viewModel.gameResult.observeAsState(initial = "")
    val deckData by viewModel.deckData.observeAsState(initial = null)
    // Observe the chips and the option to split
    val playerChips by viewModel.playerChips.observeAsState(initial = 100)
    val splitOffered by viewModel.splitOffered.observeAsState(initial = false)
    val betPlaced by viewModel.betPlaced.observeAsState(initial = false)
    val gameOver by viewModel.gameOver.observeAsState(initial = false)
    val nextRoundEnabled by viewModel.nextRoundEnabled.observeAsState(initial = false)
    val playerSplitHands by viewModel.playerSplitHands.observeAsState(initial = emptyList())
    val activeHandIndex by viewModel.activeHandIndex.observeAsState(initial = -1)
    val splitPerformed by viewModel.splitPerformed.observeAsState(initial = false)


    // Get deckId from deckData (can be null if not yet fetched)
    val deckId = deckData?.deck_id




    // This will hold the entire screen
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // <-- Add this for scrollable behavior
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Dealer's cards display
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Dealer's Cards:")

                LazyRow(horizontalArrangement = Arrangement.spacedBy((-80).dp)) {
                    items(dealerCards) { card ->
                        if (dealerCards.indexOf(card) == 0 && gameResult.isEmpty()) {
                            CardComponent(card = card, isFaceUpOverride = true) // Show back of the first card until game ends
                        } else {
                            CardComponent(card = card)
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Main player's cards display
            DisplayHand(cards = playerCards, title = "Your Cards:", isHandActive = activeHandIndex == -1)

            // Split hands display
            playerSplitHands.forEachIndexed { index, splitHand ->
                Spacer(modifier = Modifier.height(16.dp))
                DisplayHand(cards = splitHand, title = "Split Hand ${index + 1}:", isHandActive = activeHandIndex == index)
            }

            // Display Player's chips
            Text(text = "Chips: $playerChips")

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))


            // Action buttons
            if (betPlaced) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        deckId?.let {
                            viewModel.drawCardForPlayer(it)
                        }
                    }) {
                        Text("Hit")
                    }

                    Button(onClick = {
                        viewModel.stand()
                    }) {
                        Text("Stand")
                    }

                    if (splitOffered && !splitPerformed) {
                        Button(onClick = {
                            viewModel.handleSplit()
                        }) {
                            Text("Split")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Betting Component
            if (!betPlaced && gameResult.isEmpty()) {
                BettingComponent(chips = playerChips) { bet ->
                    viewModel.placeBetAsync(bet)
                }
            }



            Spacer(modifier = Modifier.height(16.dp))


            // Game status and result
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (gameResult.isNotEmpty()) {
                    Text(text = gameResult)

                    // Display Game Over text if game is over
                    if (gameOver) {
                        Text(text = "Game Over!")
                    }

                    // Display a button to start the next round if the option is enabled
                    if (nextRoundEnabled) {
                        Button(onClick = {
                            viewModel.startNextRound()
                        }) {
                            Text("Start Next Round")
                        }
                    }

                } else {
                    Text(text = "Your Turn")
                }
            }
        }
    }


}
