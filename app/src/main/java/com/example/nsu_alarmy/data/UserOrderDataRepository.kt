package com.example.nsu_alarmy.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object UserOrderDataRepository {
    val tag: String = "Firestore"

    /* 데이터 가져오기 */
    fun getOrderMenu(userId: String, onComplete: (Map<String, OrderData>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("user_data").document(userId)
            .collection("order")
            .get()
            .addOnSuccessListener { result ->
                val allOrderMap = mutableMapOf<String, OrderData>()
                // orderId ->{
                //      orderNum,
                //      가게1 -> {complete,[메뉴1,메뉴2]},
                //      가게2 -> {complete, [메뉴1,메뉴2]}  } 형태

                for (document in result.documents) {
                    val orderId = document.id // 주문 날짜
                    val rawData = document.data ?: continue // Firestore에서 가져온 데이터

                    val orderNum = (rawData["orderNum"] as? Long)?.toInt() ?: 0 // 주문 번호

                    // 가게1 -> [메뉴1,메뉴2] 형태
                    val storeMap = mutableMapOf<String, StoreOrder>()

                    for ((key, value) in rawData) {
                        if (key == "orderNum") continue

                        //가게별 주문 가져오기
                        val storeData = value as? Map<*, *> ?: continue
                        val complete = storeData["complete"] as? Boolean ?: false
                        val menuList = storeData["menuList"] as? List<*> ?: continue

                        val orderMenuList = menuList.mapNotNull { item ->
                            val itemMap = item as? Map<*, *> ?: return@mapNotNull null

                            // 옵션 가져오기
                            val optionList =
                                (itemMap["optionList"] as? List<*>)?.mapNotNull { option ->
                                    val optionMap = option as? Map<*, *> ?: return@mapNotNull null
                                    Option(
                                        optionName = optionMap["optionName"] as? String ?: "",
                                        optionPrice = (optionMap["optionPrice"] as? Long)?.toInt()
                                            ?: 0
                                    )
                                }

                            // 메뉴 가져오기
                            OrderMenu(
                                menu = itemMap["menu"] as? String ?: "",
                                menuPrice = (itemMap["menuPrice"] as? Long)?.toInt() ?: 0,
                                amount = (itemMap["amount"] as? Long)?.toInt() ?: 0,
                                optionList = optionList
                            )
                        }
                        storeMap[key] = StoreOrder(complete, orderMenuList)
                    }

                    allOrderMap[orderId] = OrderData(orderNum, storeMap)

                    Log.i(tag, "주문일시: " + orderId + " 저장 완료")
                    Log.i(tag, allOrderMap.toString())
                }
                Log.i(tag, allOrderMap.toString())
                Log.i(tag, "${userId}: 주문내역 데이터 로드 성공 ${allOrderMap.size}개")
                onComplete(allOrderMap)
            }
            .addOnFailureListener { e ->
                Log.w(tag, "주문내역 데이터 가져오기 실패 :$e")
            }

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

}