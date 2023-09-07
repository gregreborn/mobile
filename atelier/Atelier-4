package com.example.atelier_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.atelier_4.ui.theme.Atelier4Theme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Atelier4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var billText by remember { mutableStateOf(TextFieldValue()) }
                    val checkedState = remember { mutableStateOf(true) }
                    val sliderValueTip = remember { mutableStateOf(5f) }
                    val totalBill = remember { mutableStateOf(0f) }
                    val sliderValuePeople = remember { mutableStateOf(1) }

                    val taxPercentage = 0.15f //15% tax
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        Text(text = "Enter the amount of the bill")
                        TextField(
                            value = billText,
                            onValueChange = { newText ->
                                billText = newText
                                updateTotalBill(billText, sliderValueTip.value, checkedState.value, totalBill,sliderValuePeople.value)

                            },
                            label = { Text("Enter text") }
                        )
                        Text(text = "Bill: ${billText.text}$")
                        Text(text = "Should tax be added")
                        Switch(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it
                                updateTotalBill(billText, sliderValueTip.value, checkedState.value,totalBill,sliderValuePeople.value)
                            }
                        )
                        Slider(
                            value = sliderValueTip.value,
                            onValueChange = { sliderValueTip.value = it
                                updateTotalBill(billText, sliderValueTip.value, checkedState.value,totalBill,sliderValuePeople.value)
                            },
                            valueRange = 5f..20f)
                        Text(text = "Tip: ${sliderValueTip.value}")
                        TextField(
                            value = totalBill.value.toString(),
                            onValueChange = {},
                            label = { Text(text = "Total Bill")},
                            readOnly = true)
                        Slider(
                            value = sliderValuePeople.value.toFloat(),
                            onValueChange = {sliderValuePeople.value = it.toInt()
                                updateTotalBill(billText, sliderValueTip.value, checkedState.value,totalBill,sliderValuePeople.value)
                             },
                            valueRange = 1f..5f)
                        Text(text = "Personne: ${sliderValuePeople.value}")
                    }
                }
            }
        }
    }
    private fun updateTotalBill(billText: TextFieldValue, sliderValue: Float, checkedState: Boolean, totalBill: MutableState<Float>, sliderValue2: Int) {
        val bill = billText.text.toFloatOrNull() ?: 0f
        val tipPercentage = sliderValue / 100
        val billPerPeaple = sliderValue2
        val taxPercentage = if (checkedState) 0.15f else 0f

        var total = bill + (bill * tipPercentage)

        if (checkedState) {


                total += (total * taxPercentage)
                total = total / billPerPeaple

        }

        totalBill.value = total
    }


}

