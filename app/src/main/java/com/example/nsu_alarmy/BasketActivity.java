package com.example.nsu_alarmy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nsu_alarmy.data.BasketViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class BasketActivity extends AppCompatActivity {
    private static final String tag = "BasketActivity";

    private BasketViewModel basketViewModel;
    private RecyclerView mainRecyclerView;
    private BasketAdapter basketAdapter;
    private Button orderButton;

    private int currentTotalPrice = 0;
    private int currentTotalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // 사용자 아이디
        String userId = "testId";

        basketViewModel = new ViewModelProvider(this).get(BasketViewModel.class); // ViewModel 생성

        basketViewModel.loadBasketData(userId); // 장바구니 데이터 가져오기
        Log.d(tag, "groupByStoreBasket: " + basketViewModel.getFilteredList().getValue());

        // 외부 recyclerView 연결
        mainRecyclerView = findViewById(R.id.rv_each_store);
        mainRecyclerView.setHasFixedSize(true);

        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        basketAdapter = new BasketAdapter(userId, this, basketViewModel, this);
        mainRecyclerView.setAdapter(basketAdapter);


        // 총 주문 금액
        DecimalFormat formatter = new DecimalFormat("#,###");
        basketViewModel.getTotalPriceAll().observe(this, total -> {
            currentTotalPrice = total;
            TextView totalPriceAll = findViewById(R.id.total_price);
            totalPriceAll.setText(formatter.format(total) + "원");
        });
        // 총 주문 개수
        basketViewModel.getTotalAmount().observe(this, amount -> {
            currentTotalAmount = amount;
            Button orderButton = findViewById(R.id.orderButton);
            orderButton.setText("결제하기 (" + amount + "개)");
        });


        // 주문하기 버튼 클릭 시 결제 화면으로 이동
        orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(BasketActivity.this, PayActivity.class);
            // payActivity로 데이터 전달(총 주문 금액, 개수, 메뉴)
            intent.putExtra("total_price", currentTotalPrice);
            intent.putExtra("total_amount", currentTotalAmount);
            intent.putExtra("total_data", new ArrayList<>(basketViewModel.getBasketList().getValue()));
            // 장바구니 비우기
            startActivity(intent);
        });

        // 뒤로 가기 버튼 설정
        ImageView adBackButton = findViewById(R.id.btn_back);
        adBackButton.setOnClickListener(v -> {

        });

    }
}
