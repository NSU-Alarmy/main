package com.example.nsu_alarmy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nsu_alarmy.data.BasketMenu;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PayActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private String selectedPayment = ""; // 선택한 결제 수단

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 주문 내역
        ArrayList<BasketMenu> basketList = (ArrayList<BasketMenu>) getIntent().getSerializableExtra("total_data");
        Log.i("payActivity", "결제 대기 : " + basketList.toString());

        // 총 주문 금액
        TextView totalPriceView = findViewById(R.id.total_price);
        DecimalFormat formatter = new DecimalFormat("#,###");
        int totalPrice = getIntent().getIntExtra("total_price", 0);
        totalPriceView.setText(formatter.format(totalPrice) + "원 결제하기 ");

        // 총 주문 개수
        TextView totalAmountView = findViewById(R.id.total_amount);
        int totalAmount = getIntent().getIntExtra("total_amount", 0);
        totalAmountView.setText("(" + totalAmount + "개)");

        radioGroup = findViewById(R.id.paymentGroup);

        // 라디오 버튼 선택 이벤트 처리
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioToss) {
                selectedPayment = "토스";
            } else if (checkedId == R.id.radioKakao) {
                selectedPayment = "카카오페이";
            } else if (checkedId == R.id.radioNaver) {
                selectedPayment = "네이버페이";
            }
        });

        // 결제하기 버튼 클릭 시
        LinearLayout btnPay = findViewById(R.id.btn_pay_money);
        btnPay.setOnClickListener(v -> {
            if (selectedPayment.isEmpty()) {
                Toast.makeText(PayActivity.this, "결제 수단을 선택하세요!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PayActivity.this, selectedPayment + "로 결제 진행 중...", Toast.LENGTH_SHORT).show();

                // 로딩 화면으로 이동
                Intent intent = new Intent(PayActivity.this, LoadingActivity.class);
                intent.putExtra("paymentMethod", selectedPayment); // 선택한 결제 수단 전달
                intent.putExtra("order_data", basketList);
                startActivity(intent);
                finish();
            }
        });
        // 뒤로 가기 버튼 설정
        ImageView adBackButton = findViewById(R.id.btn_back);
        adBackButton.setOnClickListener(v -> finish());
    }
}
