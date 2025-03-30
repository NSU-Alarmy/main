package com.example.nsu_alarmy

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nsu_alarmy.db.Menu
import com.example.nsu_alarmy.db.MenuDataSource

class ListViewModel(val menuDataSource: MenuDataSource) : ViewModel() {
    val menuLiveData = menuDataSource.getMenuList()


    // 선택한 메뉴 ID
    private val _menuIdLiveData = MutableLiveData<String>()
    val menuId: LiveData<String> get() = _menuIdLiveData

    fun setMenuId(id: String) {
        _menuIdLiveData.value = id
    }

//    fun setMenuId(id: String){
//        menuDataSource.setMenuId(id)
//    }

    fun getMenuDetailForId(id: String): Menu? {
        return menuDataSource.getMenuDetailForId(id)
    }
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