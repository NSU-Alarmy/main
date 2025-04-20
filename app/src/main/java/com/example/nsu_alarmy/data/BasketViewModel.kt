/* 데이터 관리 로직 상태 관리 */
// 데이터 가공
// 데이터 가져오기

package com.example.nsu_alarmy.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasketViewModel : ViewModel() {
    val tag = "BasketViewModel"

    // 전체 장바구니 데이터
    private var _basketList = MutableLiveData<List<BasketMenu>>()
    val basketList: LiveData<List<BasketMenu>>
        get() = _basketList

    // 가게 별로 묶은 데이터
    private var _groupByStoreBasketMap = MutableLiveData<Map<String, List<BasketMenu>>>()
    val filteredList: LiveData<Map<String, List<BasketMenu>>>
        get() = _groupByStoreBasketMap


    // 전체 데이터 불러오기
    fun loadBasketData(userId: String) {
        BasketDataRepository.getBasketMenu(userId) { list ->
            _basketList.value = list
            _groupByStoreBasketMap.value = groupByStore(list) // 처음은 전체 목록 보여줌

            getTotalPriceAndAmount() // 총 주문 금액, 개수 업데이트
        }

    }

    // 가게별로 데이터 묶기
    private fun groupByStore(list: List<BasketMenu>): Map<String, List<BasketMenu>> {
        return list.groupBy { it.store }
    }

    // 리스트 업데이트
    fun updateBasketList(newList: List<BasketMenu>) {
        _basketList.value = newList
        _groupByStoreBasketMap.value = groupByStore(newList)

        getTotalPriceAndAmount() // 총 주문 금액, 개수 업데이트
    }

    // 리스트 삭제
    fun deleteBasketMenu(menu: BasketMenu) {
        val currentList = _basketList.value?.toMutableList() ?: return
        currentList.remove(menu)

        updateBasketList(currentList)
    }

    // 변경사항 데이터에 반영
    fun updateAmount(menu: BasketMenu, newAmount: Int) {
        val updateList = _basketList.value?.map {
            if (it.id == menu.id) {
                it.copy(amount = newAmount) // 데이터 존재하면 교체
            } else it
        } ?: return

        updateBasketList(updateList)
    }

    // 총 주문 금액
    private val _totalPriceAll = MutableLiveData<Int>()
    val totalPriceAll: LiveData<Int>
        get() = _totalPriceAll

    // 총 주문 개수
    private val _totalAmount = MutableLiveData<Int>()
    val totalAmount: LiveData<Int>
        get() = _totalAmount

    // 총 주문 금액과 개수 계산
    fun getTotalPriceAndAmount() {
        val list = _basketList.value ?: return
        var totalPriceAll = 0
        var totalAmount = 0
        for (menu in list) {
            val optionTotalPrice = menu.optionList?.sumOf { it.optionPrice } ?: 0
            var total = (menu.menuPrice + optionTotalPrice) * menu.amount
            totalPriceAll += total
            totalAmount += menu.amount
        }
        _totalPriceAll.value = totalPriceAll
        _totalAmount.value=totalAmount
    }

    /* Firestore 연동 */
    // 삭제
    fun deleteItemFromFirestore(userId: String, item: BasketMenu) {
        if (item.id.isNotEmpty()) {
            BasketDataRepository.deleteBasketMenu(userId, item.id) {
                deleteBasketMenu(item) // viewModel _리스트에서 제거
            }
        } else {
            Log.e(tag, "삭제 실패 : id가 비어있음")
        }
    }

    // 수량 업데이트
    fun updateAmountInFirestore(userId: String, item: BasketMenu, newAmount: Int) {
        if (item.id.isNotEmpty()) {
            val updateItem = item.copy(amount = newAmount)
            BasketDataRepository.updateBasketMenu(userId, updateItem) {
                updateAmount(item, newAmount)
            }
        } else {
            Log.e(tag, "업데이트 실패 : id가 비어있음")
        }
    }
}