package com.example.nsu_alarmy

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsu_alarmy.data.OrderViewModel

class OrderDetailCompleteActivity : AppCompatActivity() {
    private val tag = "OrderDetailCompleteActivity"

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var orderDetailAdapter: OrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_complete)

        val orderNum: TextView = findViewById(R.id.orderNumberTextView)
        var finalCompleteText: TextView = findViewById(R.id.completeTextView)
        val orderDate: TextView = findViewById(R.id.dateTextView)
        val totalPriceText: TextView = findViewById(R.id.totalPriceTextview)
        val payment: TextView = findViewById(R.id.paymentTextView)
        val deleteButton: Button = findViewById(R.id.deleteButton)

        val userId = intent.getStringExtra("user_id") ?: return
        val orderId = intent.getStringExtra("order_id") ?: return

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java] // ViewModel 생성

        // 총 조리 상태 초기화
        var finalComplete = false

        // orderId의 데이터 가져오기
        orderViewModel.listenOrderDataByOrderId(userId, orderId)
        Log.i(tag, "가져온 데이터: ${orderId}")
        orderViewModel.orderDataByOrderId.observe(this) { orderData ->
            Log.d(tag, "$orderData")
            // 주문 번호
            orderNum.text = "${orderData.orderNum}번"
            // 결제 수단
            payment.text = "${orderData.payment}"

            // 총 조리 상태
            finalComplete = orderViewModel.finalComplete.value?.get(orderId) ?: false
            if (finalComplete) {
                finalCompleteText.text = "조리 완료"
                finalCompleteText.setTextColor(Color.BLUE)
            } else {
                finalCompleteText.text = "조리 중"
                finalCompleteText.setTextColor(Color.RED)
            }

            // 총 결제 금액
            val totalPrice = orderViewModel.totalPrice.value?.get(orderId) ?: 0
            val formatPrice = DecimalFormat("#,###")
            totalPriceText.text = "${formatPrice.format(totalPrice)}원"
        }

        // 주문 날짜
        val formatDate =
            "${orderId.substring(0, 4)}/${orderId.substring(4, 6)}/${
                orderId.substring(6, 8)
            } " + "${orderId.substring(8, 10)}:" +
                    "${orderId.substring(10, 12)}:${orderId.substring(12, 14)}"
        orderDate.text = "${formatDate}"


        /* RecyclerView */
        // 외부 recyclerView 연결
        mainRecyclerView = findViewById(R.id.rv_store_order)
        mainRecyclerView.setHasFixedSize(true)

        // recyclerView 형태
        mainRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // recyclerView에 데이터 전달
        orderDetailAdapter = OrderDetailAdapter(this, orderViewModel, this)
        mainRecyclerView.adapter = orderDetailAdapter


        // 삭제 버튼
        // 조리가 완료된 주문만 삭제 가능
        deleteButton.setOnClickListener {
            if (finalComplete) {
                AlertDialog.Builder(this).setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { dialog, which ->
                        orderViewModel.deleteItemFromFirestore(userId, orderId){
                            Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    .setNegativeButton("취소", null).show()
            }else{
                Toast.makeText(this,"주문이 완료되지 않았습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로 가기 버튼
        val backButton: ImageButton = findViewById(R.id.btn_back)
        backButton.setOnClickListener { finish() }
    }

}