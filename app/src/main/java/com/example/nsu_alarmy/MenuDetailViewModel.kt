package com.example.nsu_alarmy

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nsu_alarmy.db.Menu
import com.example.nsu_alarmy.db.MenuDataSource


class MenuDetailViewModel (private val menuDataSource: MenuDataSource):ViewModel(){
    // Name 통해서 메뉴 데이터 가져오기
    fun getMenuDetailForName(name:String): Menu?{
        return menuDataSource.getMenuForName(name)
    }
}

class MenuDetailViewModelFactory(private val context: Context):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MenuDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MenuDetailViewModel(
                menuDataSource = MenuDataSource.getDataSource(context.resources)
            )as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}