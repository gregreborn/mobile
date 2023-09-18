package com.example.atelier5

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BillViewModel : ViewModel() {
    private val _billText = MutableLiveData(TextFieldValue())
    val billText: LiveData<TextFieldValue> = _billText

    private val _checkedState = MutableLiveData(true)
    val checkedState: LiveData<Boolean> = _checkedState

    private val _sliderValueTip = MutableLiveData(5f)
    val sliderValueTip: LiveData<Float> = _sliderValueTip

    private val _totalBill = MutableLiveData(0f)
    val totalBill: LiveData<Float> = _totalBill

    private val _sliderValuePeople = MutableLiveData(1)
    val sliderValuePeople: LiveData<Int> = _sliderValuePeople

    init {
        updateTotalBill()
    }

    fun updateBillText(newText: TextFieldValue) {
        _billText.value = newText
        updateTotalBill()
    }

    fun updateCheckedState(newState: Boolean) {
        _checkedState.value = newState
        updateTotalBill()
    }

    fun updateSliderValueTip(newValue: Float) {
        _sliderValueTip.value = newValue
        updateTotalBill()
    }

    fun updateSliderValuePeople(newValue: Int) {
        _sliderValuePeople.value = newValue
        updateTotalBill()
    }

    private fun updateTotalBill() {
        val bill = billText.value?.text?.toFloatOrNull() ?: 0f
        val tipPercentage = sliderValueTip.value?.div(100)
        val billPerPeople = sliderValuePeople.value
        val taxPercentage = if (checkedState.value == true) 0.15f else 0f

        var total = bill + (bill * tipPercentage!!)

        if (checkedState.value == true) {
            total += (total * taxPercentage)
            if (billPerPeople != null) {
                total /= billPerPeople
            }
        }

        _totalBill.value = total
    }
}

