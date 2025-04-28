package com.example.nsu_alarmy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import com.example.nsu_alarmy.data.Option
import com.example.nsu_alarmy.data.OrderData
import com.example.nsu_alarmy.data.OrderMenu
import com.example.nsu_alarmy.data.StoreOrder
import com.example.nsu_alarmy.data.UserOrderDataRepository.tag
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val basketButton: Button = findViewById(R.id.basketBtn)
        val orderListButton: LinearLayout = findViewById(R.id.main_list)

        basketButton.setOnClickListener {
            val intent = Intent(this, BasketActivity::class.java)
            startActivity(intent)
        }
        orderListButton.setOnClickListener {
            val intent = Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }

    }
}
