package com.example.nsu_alarmy.data

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

object UserOrderDataRepository {
    val tag: String = "Firestore"

    /* 전체 주문 데이터 가져오기 */
    fun listenOrderChanges(
        userId: String,
        onUpdate: (updateData: Map<String, OrderData>, removeDataId: List<String>, Boolean) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        db.collection("user_data").document(userId)
            .collection("order")
            .addSnapshotListener { snapshots, error ->
                // 오류 발생
                if (error != null) {
                    Log.w(tag, "Listen failed: $error")
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    // 처음 데이터를 가져오는지 확인
                    val isInitialLoad = snapshots.documentChanges.size == snapshots.size()

                    val updateData = mutableMapOf<String, OrderData>()
                    val removeDataIdList = mutableListOf<String>()

                    val allOrderMap = mutableMapOf<String, OrderData>()
                    // orderId ->{
                    //      orderNum,
                    //      payment,
                    //      가게1 -> {complete,[메뉴1,메뉴2]},
                    //      가게2 -> {complete, [메뉴1,메뉴2]}  } 형태

                    if (isInitialLoad) {
                        // 전체 데이터 가져오기 _초기 데이터인 경우
                        for (doc in snapshots.documents) {
                            val orderId = doc.id
                            val data = doc.data ?: continue
                            val orderData = parseOrderData(data)
                            allOrderMap[orderId] = orderData
                            Log.i(tag, "주문일시: " + orderId + " 가져오기 완료")
                        }
                        onUpdate(allOrderMap, removeDataIdList, isInitialLoad)
                    } else {
                        // 변경된 데이터만 가져오기
                        for (changeDoc in snapshots.documentChanges) {
                            val orderId = changeDoc.document.id // 변경된 데이터의 orderId
                            when (changeDoc.type) {
                                // 데이터가 변경된 경우
                                DocumentChange.Type.ADDED,
                                DocumentChange.Type.MODIFIED -> {
                                    val data = changeDoc.document.data // firestore에서 가져온 데이터
                                    val orderData = parseOrderData(data)
                                    updateData[orderId] = orderData
                                    Log.i(tag, "변경된 데이터 orderId: " + orderId + " 가져오기 완료")
                                }
                                //데이터가 삭제된 경우
                                DocumentChange.Type.REMOVED -> {
                                    removeDataIdList.add(orderId)
                                    Log.i(tag, "삭제된 데이터 orderId: " + orderId)
                                }
                            }
                        }
                        onUpdate(updateData, removeDataIdList, isInitialLoad)
                    }
                    Log.i(tag, "${userId}: 주문내역 데이터 로드 성공 ${snapshots.size()}개")
                }

            }
    }

    // 데이터 가공
    fun parseOrderData(data: Map<String, Any>): OrderData {
        val orderNum = (data["orderNum"] as? Long)?.toInt() ?: 0 // 주문 번호
        val payment = data["payment"] as? String ?: "" // 결제 수단

        // 가게1 -> [메뉴1,메뉴2] 형태
        val storeMap = mutableMapOf<String, StoreOrder>()

        for ((key, value) in data) {
            if (key == "orderNum" || key == "payment") continue

            //가게별 데이터 가져오기
            val storeData = value as? Map<*, *> ?: continue
            val complete = storeData["complete"] as? Boolean ?: false

            // 메뉴 리스트
            val menuList = (storeData["menuList"] as? List<*>)?.mapNotNull { item ->
                val itemMap = item as? Map<*, *> ?: return@mapNotNull null
                // 옵션 가져오기
                val optionList =
                    (itemMap["optionList"] as? List<*>)?.mapNotNull { option ->
                        val optionMap =
                            option as? Map<*, *> ?: return@mapNotNull null
                        Option(
                            optionName = optionMap["optionName"] as? String ?: "",
                            optionPrice = (optionMap["optionPrice"] as? Long)?.toInt()
                                ?: 0
                        )
                    } ?: emptyList()

                OrderMenu(
                    menu = itemMap["menu"] as? String ?: "",
                    menuPrice = (itemMap["menuPrice"] as? Long)?.toInt() ?: 0,
                    amount = (itemMap["amount"] as? Long)?.toInt() ?: 0,
                    optionList = optionList
                )
            } ?: emptyList()

            storeMap[key] = StoreOrder(complete, menuList)
        }
        return OrderData(orderNum, payment, storeMap)
    }


    /* 데이터 삭제하기 */
    fun deleteOrderMenu(userId: String, orderId: String, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("user_data").document(userId)
            .collection("order").document(orderId)

        docRef.delete().addOnSuccessListener {
            onComplete()
        }
            .addOnFailureListener { e ->
                Log.e(tag, "${orderId}: 데이터 삭제 실패")
            }
    }

    /* 특정 orderId의 주문 데이터 실시간으로 가져오기 */
    fun getOrderDataByOrderId(userId: String, orderId: String, onUpdate: (OrderData) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("user_data").document(userId)
            .collection("order").document(orderId)

        docRef.addSnapshotListener { snapshot, error ->
            // 오류 발생
            if (error != null) {
                Log.e(tag, "Listen failed $error")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val orderNum = snapshot.getLong("orderNum")?.toInt() ?: 0
                val payment = snapshot.getString("payment") ?: ""

                // 가게1 -> [메뉴1,메뉴2] 형태
                val storeMap = mutableMapOf<String, StoreOrder>()

                for ((key, value) in snapshot.data ?: emptyMap<String, Any>()) {
                    if (key == "orderNum" || key == "payment") continue

                    //가게별 데이터 가져오기
                    val storeData = value as? Map<*, *> ?: continue
                    val complete = storeData["complete"] as? Boolean ?: false

                    // 메뉴 리스트
                    val menuList = (storeData["menuList"] as? List<*>)?.mapNotNull { item ->
                        val itemMap = item as? Map<*, *> ?: return@mapNotNull null
                        // 옵션 가져오기
                        val optionList =
                            (itemMap["optionList"] as? List<*>)?.mapNotNull { option ->
                                val optionMap =
                                    option as? Map<*, *> ?: return@mapNotNull null
                                Option(
                                    optionName = optionMap["optionName"] as? String ?: "",
                                    optionPrice = (optionMap["optionPrice"] as? Long)?.toInt()
                                        ?: 0
                                )
                            } ?: emptyList()

                        OrderMenu(
                            menu = itemMap["menu"] as? String ?: "",
                            menuPrice = (itemMap["menuPrice"] as? Long)?.toInt() ?: 0,
                            amount = (itemMap["amount"] as? Long)?.toInt() ?: 0,
                            optionList = optionList
                        )
                    } ?: emptyList()

                    storeMap[key] = StoreOrder(complete, menuList)
                }
                val orderData = OrderData(orderNum, payment, storeMap)
                if (storeMap.isNotEmpty()) {
                    onUpdate(orderData)
                } else {
                    Log.d(tag, "${orderId} 데이터 비어있음")
                }

                onUpdate(orderData)
                Log.i(tag, "주문일시: " + orderId + " 가져오기 완료 \n" + orderData)
            }
        }
    }
}