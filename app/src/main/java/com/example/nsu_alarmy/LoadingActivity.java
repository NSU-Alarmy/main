package com.example.nsu_alarmy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page); // 로딩 화면의 레이아웃

        Log.i("payActivity", "결제 중 : " + getIntent().getSerializableExtra("order_data"));

        

        // 3초 후 ReceiptActivity로 이동
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoadingActivity.this, ReceiptActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3초(3000ms) 대기


    }
}
