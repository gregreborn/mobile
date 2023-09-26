package com.example.atelier7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.atelier7.ui.theme.Atelier7Theme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: BillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            BillViewModelFactory(application)
        )[BillViewModel::class.java]

        setContent {
            Atelier7Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "transactions") {
                        composable("transactions") {
                            TransactionsScreen(viewModel, navController)
                        }
                        composable("add_transaction") {
                            AddTransactionScreen(viewModel, navController)
                        }
                        composable(
                            "update_transaction/{transactionId}"
                        ) { backStackEntry ->
                            val transactionIdString = backStackEntry.arguments?.getString("transactionId")
                            val transactionId = transactionIdString?.toIntOrNull()
                            if (transactionId != null && transactionId != 0) {
                                UpdateTransactionScreen(viewModel, navController, id = transactionId)
                            } else {
                                navController.navigate("transactions") {
                                    popUpTo("transactions") { inclusive = true }
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}
