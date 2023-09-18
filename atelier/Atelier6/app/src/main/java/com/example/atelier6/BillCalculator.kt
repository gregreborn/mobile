package com.example.atelier6

// BillCalculator.kt


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BillCalculator(viewModel: BillViewModel) {
    val billText by viewModel.billText.observeAsState(TextFieldValue())
    val checkedState by viewModel.checkedState.observeAsState(true)
    val sliderValueTip by viewModel.sliderValueTip.observeAsState(5f)
    val totalBill by viewModel.totalBill.observeAsState(0f)
    val sliderValuePeople by viewModel.sliderValuePeople.observeAsState(1)
    val allTransactions by viewModel.getAllTransactions().observeAsState(listOf())

    LazyColumn(modifier = Modifier.padding(all = 16.dp)) {
        item {
            Text(text = "Enter the amount of the bill")
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = billText,
                onValueChange = { newText -> viewModel.updateBillText(newText) },
                label = { Text("Enter amount") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Bill: $${billText.text}")
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Should tax be added")
                Switch(checked = checkedState,
                    onCheckedChange = { newState -> viewModel.updateCheckedState(newState) })
            }
            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValueTip,
                onValueChange = { newValue -> viewModel.updateSliderValueTip(newValue) },
                valueRange = 5f..20f
            )
            Text(text = "Tip: ${sliderValueTip}%")
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = String.format("%.2f", totalBill),
                onValueChange = {},
                label = { Text(text = "Total Bill") },
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValuePeople.toFloat(),
                onValueChange = { newValue -> viewModel.updateSliderValuePeople(newValue.toInt()) },
                valueRange = 1f..5f
            )
            Text(text = "People: $sliderValuePeople")
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveTransaction() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Save Transaction")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


        items(allTransactions) { transaction ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(transaction.billAmount, Modifier.weight(3f))
                Text(transaction.totalBill.toString(), Modifier.weight(3f))
                Button(
                    onClick = { viewModel.deleteTransactionById(transaction.id) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                     shape = RoundedCornerShape(8.dp),

                ) {
                    Text("X", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


