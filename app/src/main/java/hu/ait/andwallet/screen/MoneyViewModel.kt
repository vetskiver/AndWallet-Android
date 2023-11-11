package hu.ait.andwallet.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hu.ait.andwallet.data.MoneyItem
import hu.ait.andwallet.data.MoneyType

class MoneyListViewModel : ViewModel() {
    private var _moneyList = mutableStateListOf<MoneyItem>()
    fun getAllItems(): List<MoneyItem> {
        return _moneyList
    }
    fun getExpense(): Int {
        var expense = 0
        _moneyList.forEach {
            if (it.type == MoneyType.EXPENSE) {
                expense += it.amount
            }
        }
        return expense
    }
    fun getIncome(): Int {
        var income = 0
        _moneyList.forEach {
            if (it.type == MoneyType.INCOME) {
                income += it.amount
            }
        }
        return income
    }
    fun addToMoneyList(moneyItem: MoneyItem) {
        _moneyList.add(moneyItem)
    }
    fun removeItem(moneyItem: MoneyItem) {
        _moneyList.remove(moneyItem)
    }
    fun clearAllItems() {
        _moneyList.clear()
    }
    fun editMoneyItem(oldMoneyItem: MoneyItem, updatedMoneyItem: MoneyItem) {
        val index = _moneyList.indexOf(oldMoneyItem)
        if (index != -1) {
            _moneyList[index] = updatedMoneyItem
        }
    }
}