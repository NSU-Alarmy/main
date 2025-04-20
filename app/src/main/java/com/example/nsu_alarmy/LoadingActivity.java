package com.example.nsu_alarmy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nsu_alarmy.data.BasketMenu;
import com.example.nsu_alarmy.data.Option;
import com.example.nsu_alarmy.data.OrderMenu;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadingActivity extends AppCompatActivity {
    private String tag = "PayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page); // 로딩 화면의 레이아웃

        // 주문 날짜
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String today = current.format(dateFormat);
        Log.i(tag, "주문 날짜: " + today);

        // 주문 데이터
        ArrayList<BasketMenu> orderList = (ArrayList<BasketMenu>) getIntent().getSerializableExtra("order_data");

        /* 데이터 가공 */
        // 메뉴를 가게를 기준으로 묶어 리스트로 저장
        Map<String, List<OrderMenu>> groupByStoreMap = new HashMap<>();

        for (BasketMenu item : orderList) {
            String storeName = item.getStore();

            OrderMenu orderMenu = new OrderMenu(
                    item.getMenu(),
                    item.getMenuPrice(),
                    item.getAmount(),
                    item.getOptionList()
            );

            if (!groupByStoreMap.containsKey(storeName)) {
                groupByStoreMap.put(storeName, new ArrayList<>());
            }

            groupByStoreMap.get(storeName).add(orderMenu);
        }
        Log.i(tag, "결제 중: " + groupByStoreMap);

        /* Firestore에 데이터 넣기 */
        // 데이터를 firestore order에 저장
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = "testId";
        String docId = today.replaceAll("[^0-9]", ""); //yyyyMMddHHmmss형태
        DocumentReference docRef = db.collection("user_data").document(userId)
                .collection("order").document(docId);
        docRef.set(groupByStoreMap)
                .addOnSuccessListener(unused -> {
                    Log.i(tag, "주문 정보 저장 성공");

                    // firestore basket 문서 삭제 ===============================================
//                    CollectionReference basketRef = db.collection("user_data").document(userId)
//                            .collection("basket");
//                    basketRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
//                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
//                            doc.getReference().delete();
//                        }
//                    }).addOnFailureListener(e -> {
//                        Log.e(tag, "장바구니 삭제 실패");
//                    });

                }).addOnFailureListener(e -> {
                    Log.e(tag, "주문 정보 저장 실패");
                });


        // 3초 후 ReceiptActivity로 이동
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoadingActivity.this, ReceiptActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3초(3000ms) 대기


    }
}
