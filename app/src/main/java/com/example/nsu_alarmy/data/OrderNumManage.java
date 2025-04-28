package com.example.nsu_alarmy.data;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class OrderNumManage {
    public static void getOrderNum(OnOrderNumListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("store_order").document("orderNum");

        db.runTransaction((Transaction.Function<Integer>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(docRef);
            Long currentNum = snapshot.getLong("number");
            int newNum = (currentNum != null ? currentNum.intValue() : 0) + 1;

            transaction.update(docRef, "number", newNum); // 다음 숫자 넣기
            return newNum;
        }).addOnSuccessListener(newNum -> {
            listener.onSuccess(newNum);
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "주문번호 가져오기 실패");
            listener.onFailure(e);
        });

    }

    public interface OnOrderNumListener {
        void onSuccess(int newNum);

        void onFailure(Exception e);
    }
}
