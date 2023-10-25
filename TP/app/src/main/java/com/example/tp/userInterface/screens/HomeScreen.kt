package com.example.tp.userInterface.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    // Other UI elements
    Button(onClick = {
        navController.navigate("gameplayScreen")
    }) {
        Text("Start Game")
    }
}
