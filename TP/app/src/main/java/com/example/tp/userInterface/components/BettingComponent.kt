package com.example.tp.userInterface.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BettingComponent(
    chips: Int,
    onBetPlaced: (Int) -> Unit
) {
    var betAmount by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) } // To show error for invalid bet

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Chips: $chips",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = betAmount,
            onValueChange = { betAmount = it; showError = false }, // Reset error when user starts typing
            label = { Text("Bet Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = showError // Highlight field in error if showError is true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val bet = betAmount.toIntOrNull() ?: 0
            if (bet in 1..chips) {
                onBetPlaced(bet)
                betAmount = "" // Reset bet amount for next round
            } else {
                showError = true // Set error to true for invalid bets
            }
        }) {
            Text("Place Bet")
        }
    }
}



