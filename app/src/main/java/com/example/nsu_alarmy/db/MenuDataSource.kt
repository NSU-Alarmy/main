package com.example.nsu_alarmy.db

import android.content.res.Resources
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MenuDataSource(resources: Resources) {
    private var initialMenuList = burgerList(resources)
    private val menusLiveData = MutableLiveData(initialMenuList)

    @MainThread
    fun getMenuList(): LiveData<List<Menu>> {
        return menusLiveData
    }

    fun getMenuForName(name: String): Menu? {
        menusLiveData.value?.let { hamburgers ->
            return hamburgers.firstOrNull { it.id == name }
        }
        return null
    }

    companion object {
        private var INSTANCE: MenuDataSource? = null

        fun getDataSource(resources: Resources): MenuDataSource {
            return synchronized(MenuDataSource::class) {
                val newInstance = INSTANCE ?: MenuDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }


}