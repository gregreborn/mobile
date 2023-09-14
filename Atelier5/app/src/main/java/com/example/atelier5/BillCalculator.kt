// BillCalculator.kt
package com.example.atelier5

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun BillCalculator(viewModel: BillViewModel) {
    val billText by viewModel.billText.observeAsState(TextFieldValue())
    val checkedState by viewModel.checkedState.observeAsState(true)
    val sliderValueTip by viewModel.sliderValueTip.observeAsState(5f)
    val totalBill by viewModel.totalBill.observeAsState(0f)
    val sliderValuePeople by viewModel.sliderValuePeople.observeAsState(1)

    Column(modifier = Modifier.padding(all = 16.dp)) {
        Text(text = "Enter the amount of the bill")
        TextField(
            value = billText,
            onValueChange = { newText -> viewModel.updateBillText(newText) },
            label = { Text("Enter amount") }
        )
        Text(text = "Bill: $${billText.text}")
        Text(text = "Should tax be added")
        Switch(
            checked = checkedState,
            onCheckedChange = { newState -> viewModel.updateCheckedState(newState) }
        )
        Slider(
            value = sliderValueTip,
            onValueChange = { newValue -> viewModel.updateSliderValueTip(newValue) },
            valueRange = 5f..20f
        )
        Text(text = "Tip: ${sliderValueTip}%")
        TextField(
            value = String.format("%.2f", totalBill),
            onValueChange = {},
            label = { Text(text = "Total Bill") },
            readOnly = true
        )
        Slider(
            value = sliderValuePeople.toFloat(),
            onValueChange = { newValue -> viewModel.updateSliderValuePeople(newValue.toInt()) },
            valueRange = 1f..5f
        )
        Text(text = "People: $sliderValuePeople")
    }
}
