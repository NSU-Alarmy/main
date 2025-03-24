package com.example.nsu_alarmy

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nsu_alarmy.db.MenuDataSource

class ListViewModel(val menuDataSource: MenuDataSource) : ViewModel() {
    val menuLiveData = menuDataSource.getMenuList()
}

class ListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(
                menuDataSource = MenuDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}