/* 데이터 가공 처리 */
// Firestore에서 가져오는 데이터 형태
// orderId ->{
//      orderNum,
//      가게1 -> {complete,[메뉴1,메뉴2]},
//      가게2 -> {complete, [메뉴1,메뉴2]}  }

package com.example.nsu_alarmy.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {
    val tag = "OrderViewModel"

    // 전체 주문내역 데이터
    private val _orderDataMap = MutableLiveData<Map<String, OrderData>>()
    val orderDataMap: LiveData<Map<String, OrderData>>
        get() = _orderDataMap

    // 전체 데이터 불러오기
    fun loadOrderData(userId: String) {
        UserOrderDataRepository.getOrderMenu(userId) { dataMap ->
            _orderDataMap.value = dataMap
            Log.i(tag, "가져온 데이터: " + orderDataMap.toString())
        }
    }

    // 데이터 삭제
    fun deleteOrderByOrderId(userId: String, orderId: String) {
        val currentItem = _orderDataMap.value?.toMutableMap() ?: return
        currentItem.remove(orderId)
        _orderDataMap.value = currentItem
    }


    /* 총 주문 금액, 개수, 최종 주문 상태 */
    // 총 주문 금액
    private val _totalPrice = MutableLiveData<Map<String, Int>>()
    val totalPrice: LiveData<Map<String, Int>> get() = _totalPrice

    // 총 주문 개수
    private val _totalAmount = MutableLiveData<Map<String, Int>>()
    val totalAmount: LiveData<Map<String, Int>> get() = _totalAmount

    // 최종 주문 상태(모든 가게의 주문 상태가 True면 True)
    private val _finalComplete = MutableLiveData<Map<String, Boolean>>()
    val finalComplete: LiveData<Map<String, Boolean>> get() = _finalComplete


    // 총 주문 금액과 개수, 주문 상태 계산
    fun getTotalOfOrderData(orderId: String) {
        val orderDataMap = _orderDataMap.value?.get(orderId) ?: return

        var totalPrice = 0
        var totalAmount = 0
        var finalComplete = true

        var totalPriceEachStore = 0
        var totalAmountEachStore = 0


        for ((_, storeOrder) in orderDataMap.storeMap ?: emptyMap()) {
            finalComplete = finalComplete && storeOrder.complete

            for (menu in storeOrder.menuList ?: emptyList()) {

                val optionTotalPrice = menu.optionList?.sumOf { it.optionPrice } ?: 0
                val price = (menu.menuPrice + optionTotalPrice) * menu.amount
                totalPriceEachStore += price
                totalAmount += menu.amount
            }
            totalPrice += totalPriceEachStore
        }

        val totalPriceMap = _totalPrice.value?.toMutableMap() ?: mutableMapOf()
        val totalAmountMap = _totalAmount.value?.toMutableMap() ?: mutableMapOf()
        val finalCompleteMap = _finalComplete.value?.toMutableMap() ?: mutableMapOf()

        totalPriceMap[orderId] = totalPrice
        totalAmountMap[orderId] = totalAmount
        finalCompleteMap[orderId] = finalComplete

        _totalPrice.value = totalPriceMap
        _totalAmount.value = totalAmountMap
        _finalComplete.value = finalCompleteMap
    }


    /* Firestore 연동 */
    // 삭제
    fun deleteItemFromFirestore(userId: String, orderId: String) {
        UserOrderDataRepository.deleteOrderMenu(userId, orderId) {
            deleteOrderByOrderId(userId, orderId) // 로컬에서도 데이터 삭제
        }
    }

}