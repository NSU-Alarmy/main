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
    val orderDataMap: LiveData<Map<String, OrderData>> get() = _orderDataMap

    // 전체 데이터 가져오기(+실시간 업데이트)
    fun listenAllOrderData(userId: String) {
        UserOrderDataRepository.listenOrderChanges(userId) { updateData, removeDataId, isInitial ->
            val currentMap = if (isInitial) {
                mutableMapOf()
            } else {
                _orderDataMap.value?.toMutableMap() ?: mutableMapOf()
            }

            // 업데이트된 데이터 덮어쓰기
            for ((orderId, orderData) in updateData) {
                currentMap[orderId] = orderData
            }
            // 데이터 삭제
            for (orderId in removeDataId) {
                currentMap.remove(orderId)
            }

            // 업데이트된 데이터의 totalData(금액, 개수, 조리상태) 계산
            updateData.forEach { orderId, orderData ->
                if (orderData != null) {
                    getTotalOfOrderData(orderId, orderData)
                }
            }

            // 계산 후, LiveData 갱신
            _orderDataMap.postValue(currentMap)
        }
    }

    // 데이터 삭제
    fun deleteOrderByOrderId(orderId: String) {
        val currentItem = _orderDataMap.value?.toMutableMap() ?: return
        currentItem.remove(orderId)
        _orderDataMap.value = currentItem
    }


    /* 총 주문 금액, 개수, 최종 주문 상태 */
    // 총 주문 금액
    private val _totalPrice = MutableLiveData<Map<String, Int>>(mutableMapOf())
    val totalPrice: LiveData<Map<String, Int>> get() = _totalPrice

    // 총 주문 개수
    private val _totalAmount = MutableLiveData<Map<String, Int>>(mutableMapOf())
    val totalAmount: LiveData<Map<String, Int>> get() = _totalAmount

    // 최종 주문 상태(모든 가게의 주문 상태가 True면 True)
    private val _finalComplete = MutableLiveData<Map<String, Boolean>>(mutableMapOf())
    val finalComplete: LiveData<Map<String, Boolean>> get() = _finalComplete

    // 총 주문 금액과 개수, 주문 상태 계산
    fun getTotalOfOrderData(orderId: String, orderDataMap: OrderData) {
        var totalPrice = 0
        var totalAmount = 0
        var finalComplete = true

        for ((_, storeOrder) in orderDataMap.storeMap ?: emptyMap()) {
            var totalPriceEachStore = 0

            // 각 가게의 complete가 false면 finalComplete를 false로 설정
            if (storeOrder.complete == false) {
                finalComplete = false
            }

            storeOrder.menuList.orEmpty().forEach { menu ->
                val optionTotalPrice = menu.optionList?.sumOf { it.optionPrice } ?: 0
                val price = (menu.menuPrice + optionTotalPrice) * menu.amount
                totalPriceEachStore += price
                totalAmount += menu.amount
            }
            totalPrice += totalPriceEachStore
        }
        // 기존 데이터를 가져와서 업데이트
        val totalPriceMap = _totalPrice.value?.toMutableMap() ?: mutableMapOf()
        val totalAmountMap = _totalAmount.value?.toMutableMap() ?: mutableMapOf()
        val finalCompleteMap = _finalComplete.value?.toMutableMap() ?: mutableMapOf()

        totalPriceMap[orderId] = totalPrice
        totalAmountMap[orderId] = totalAmount
        finalCompleteMap[orderId] = finalComplete

        // 값 업데이트 후 postValue 호출
        _totalPrice.value = totalPriceMap
        _totalAmount.value = totalAmountMap
        _finalComplete.value = finalCompleteMap
    }


    /* 특정 orderId의 데이터만 가져오기 */
    private val _orderDataByOrderId = MutableLiveData<OrderData>()
    val orderDataByOrderId: LiveData<OrderData> get() = _orderDataByOrderId

    fun listenOrderDataByOrderId(userId: String, orderId: String) {
        UserOrderDataRepository.getOrderDataByOrderId(userId, orderId) { orderData ->
            getTotalOfOrderData(orderId, orderData)
            _orderDataByOrderId.postValue(orderData)
        }
    }

    /* Firestore 연동 */
    // 삭제
    fun deleteItemFromFirestore(userId: String, orderId: String, onComplete: () -> Unit) {
        UserOrderDataRepository.deleteOrderMenu(userId, orderId) {
            deleteOrderByOrderId(orderId) // 로컬에서도 데이터 삭제
            onComplete()
        }
    }

}