package com.example.tp.userInterface.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsDrawerComponent(cardsCount: Map<String, Int>, totalCards: Int) {
    // A simple column to list out probabilities
    Column(modifier = Modifier.padding(16.dp)) {
        cardsCount.forEach { (cardType, count) ->
            val probability = calculateProbability(count, totalCards)
            // Display the card type and its probability
            Text(text = "$cardType: ${probability}%")
        }
    }
}

fun calculateProbability(count: Int, total: Int): Double {
    return (count.toDouble() / total.toDouble()) * 100
}
