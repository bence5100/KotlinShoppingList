package com.example.kotlinshoppinglist
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingViewModel (application: Application) : AndroidViewModel(application) {
    private val dao = ShoppingDatabase.getDatabase(application).ShoppingDao()

    val items = dao.getALLItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()

    )

    fun addItem(name: String, quality: Int){
        viewModelScope.launch {
            dao.insertItem(ShoppingItem(name = name, quantity = quality))
        }

    }

    fun toggleItemChecked(item: ShoppingItem){
        viewModelScope.launch {
            dao.updateItem(item.copy(isChecked = !item.isChecked))
        }
    }

    fun updateItem(item: ShoppingItem, newName: String, newQuality: Int){
        viewModelScope.launch {
            dao.updateItem(item.copy(name = newName, quantity = newQuality))
        }
    }

    fun deleteItem(item: ShoppingItem){
        viewModelScope.launch {
            dao.deleteItem(item)
        }
    }
}