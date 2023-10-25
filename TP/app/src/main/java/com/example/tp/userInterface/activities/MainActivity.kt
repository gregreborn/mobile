package com.example.tp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp.api.ApiClient
import com.example.tp.data.db.AppDatabase
import com.example.tp.data.db.dao.StatisticsDao
import com.example.tp.data.model.Card
import com.example.tp.data.repository.GameRepository
import com.example.tp.userInterface.screens.HomeScreen
import com.example.tp.userInterface.screens.GameplayScreen
import com.example.tp.userInterface.components.StatisticsDrawerComponent
import com.example.tp.ui.theme.TPTheme
import com.example.tp.userInterface.viewmodels.GameViewModel
import com.example.tp.userInterface.viewmodels.GameViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var database: AppDatabase
    private lateinit var statisticsDao: StatisticsDao
    private lateinit var gameRepository: GameRepository
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(applicationContext)
        statisticsDao = database.statisticsDao()
        gameRepository = GameRepository(ApiClient.apiService, statisticsDao)

        val viewModelFactory = GameViewModelFactory(gameRepository)
        gameViewModel = ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]

        setContent {
            TPTheme {
                NavigationSystem(gameViewModel)
            }
        }
    }


}


@Composable
fun NavigationSystem(gameViewModel: GameViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") { HomeScreen(navController = navController) }
        composable("gameplayScreen") { GameplayScreen(navController = navController, viewModel = gameViewModel) }
    }
}



