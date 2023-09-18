package com.example.atelier6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.atelier6.ui.theme.Atelier6Theme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: BillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            BillViewModelFactory(application)
        )[BillViewModel::class.java]

        setContent {
            Atelier6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BillCalculator(viewModel)
                }
            }
        }
    }
}


