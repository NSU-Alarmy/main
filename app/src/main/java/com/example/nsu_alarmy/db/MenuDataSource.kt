/* 데이터 이용 메소드 */
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


//    // 선택한 메뉴 ID
//    private val _menuIdLiveData = MutableLiveData<String>()
//    val menuIdLiveData: LiveData<String> get() = _menuIdLiveData
//
//    fun setMenuId(id: String) {
//        _menuIdLiveData.value = id
//    }

    fun getMenuDetailForId(id: String): Menu? {
        menusLiveData.value?.let {
            return menusLiveData.value?.find { it.id == id }
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