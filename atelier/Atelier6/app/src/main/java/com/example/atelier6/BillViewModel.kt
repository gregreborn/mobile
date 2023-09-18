package com.example.atelier6

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.atelier6.database.AppDatabase
import com.example.atelier6.database.TransactionBill
import com.example.atelier6.database.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BillViewModel (application: Application) : ViewModel() {
    private val transactionDAO: TransactionDao
    private val allTransaction: LiveData<List<TransactionBill>>


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
        val database = AppDatabase.getDatabase((application))
        transactionDAO = database.transactionDao()
        allTransaction = transactionDAO.getAllTransactions()
    }

    fun getAllTransactions(): LiveData<List<TransactionBill>> {
        return allTransaction
    }

    fun deleteTransactionById(transactionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDAO.deleteTransactionById(transactionId)
        }
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

    fun saveTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            val billAmountText = billText.value?.text ?: ""
            val tipPercentageValue = sliderValueTip.value ?: 0f
            val numberOfPeopleValue = sliderValuePeople.value ?: 1
            val shouldAddTax = checkedState.value == true
            val totalBillValue = totalBill.value ?: 0f

            val transaction = TransactionBill(
                id = 0,  // Room will auto-generate this
                billAmount = billAmountText,
                isTaxAdded = shouldAddTax,
                tipPercentage = tipPercentageValue,
                totalBill = totalBillValue,
                numberOfPeople = numberOfPeopleValue
            )
            transactionDAO.insertTransaction(transaction)
        }
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

class BillViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BillViewModel::class.java)) {
            return BillViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}





