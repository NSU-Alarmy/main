package com.example.nsu_alarmy;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nsu_alarmy.data.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private static final String tag = "OrderHistoryActivity";

    private OrderViewModel orderViewModel;
    private RecyclerView mainRecyclerView;
    private OrderAdapter orderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // 사용자 아이디
        String userId = "testId";

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class); // ViewModel 생성

        // 외부 recyclerView 연결
        mainRecyclerView = findViewById(R.id.rv_each_date);
        mainRecyclerView.setHasFixedSize(true);

        // recyclerView 형태
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mainRecyclerView.addItemDecoration(decoration);

        orderAdapter = new OrderAdapter(userId, this, orderViewModel, this);
        mainRecyclerView.setAdapter(orderAdapter);


        orderViewModel.loadOrderData(userId); // 주문 내역 데이터 가져오기
        Log.d(tag, "가져온 데이터(액티비티):" + orderViewModel.getOrderDataMap().getValue());


        // 뒤로 가기 버튼 설정
        ImageView adBackButton = findViewById(R.id.btn_back);
        adBackButton.setOnClickListener(v -> finish());
    }

}

