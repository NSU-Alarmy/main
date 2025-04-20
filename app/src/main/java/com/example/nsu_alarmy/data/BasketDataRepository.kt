/* Firestore의 장바구니 데이터 수정 */
package com.example.nsu_alarmy.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object BasketDataRepository {
    val tag: String = "BasketDataResource"

    /* 데이터 가져오기 */
    fun getBasketMenu(userId: String, onComplete: (List<BasketMenu>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

//        아이디로 장바구니 데이터 가져오기
//        val userId = FirebaseAuth.getInstance().currentUser

        db.collection("user_data")
            .document(userId).collection("basket")
            .get()
            .addOnSuccessListener { result ->
                val basketList = result.documents.mapNotNull { doc ->
                    Log.d("Firestore", doc.data.toString())
                    try {
                        val basket = doc.toObject(BasketMenu::class.java)
                        basket?.copy(id = doc.id) //doc_id 수동 저장
                        basket
                    } catch (e: Exception) {
                        Log.w(tag, "장바구니 데이터 변환 실패: ${doc.id}:${doc.data}")
                        null
                    }
                }
                Log.i("Firestore", "${userId} : 장바구니 데이터 로드 성공 ${basketList.size}개")
                onComplete(basketList)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "장바구니 데이터 가져오기 실패: $e")
                onComplete(emptyList())
            }

    }

    /* 데이터 삭제하기 */
    fun deleteBasketMenu(userId: String, id: String, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("user_data")
            .document(userId).collection("basket")
            .document(id)

        docRef.delete()
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e(tag, "삭제 실패", e)
            }

    }

    /* 데이터 업데이트 */
    fun updateBasketMenu(userId: String, updateMenu: BasketMenu, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("user_data")
            .document(userId).collection("basket")
            .document(updateMenu.id)

        docRef.set(updateMenu).addOnSuccessListener { onComplete() }
            .addOnFailureListener { e -> Log.e(tag, "업데이트 실패", e) }
    }

}