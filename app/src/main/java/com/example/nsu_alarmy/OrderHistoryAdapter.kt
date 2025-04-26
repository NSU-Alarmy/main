package com.example.nsu_alarmy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.nsu_alarmy.data.OrderData
import com.example.nsu_alarmy.data.OrderViewModel

class OrderHistoryAdapter(
    private val userId: String,
    private val context: Context,
    private val orderViewModel: OrderViewModel,
    private val lifecycleOwner: LifecycleOwner

) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {
    private val tag = "OrderAdapter"

    private var orderDataMap: Map<String, OrderData> = HashMap()
    private var orderIdList: List<String> = listOf()

    /* 어댑터 초기화 */
    init {
        orderViewModel.orderDataMap.observe(lifecycleOwner) { dataMap ->
            if (dataMap != orderDataMap) {
                orderDataMap = dataMap
                Log.i(tag, "데이터 갱신: $dataMap")

                orderIdList = dataMap.keys.sortedDescending() // 날짜 내림차순으로 정렬

                notifyDataSetChanged() // 데이터 변경 시 RecyclerView 갱신
            }
        }
    }


    /* RecyclerView 형태 설정 */
    /* item_order.xml 형태로 뷰 생성 */
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderDate: TextView = view.findViewById(R.id.dateTextView)
        val complete: TextView = view.findViewById(R.id.completeTextView)
        val storeImage: ImageView = view.findViewById(R.id.storeImageView)
        val orderStore: TextView = view.findViewById(R.id.storeTextView)
        val orderMenu: TextView = view.findViewById(R.id.menuTextView)
        val totalPrice: TextView = view.findViewById(R.id.priceTextView)
    }


    // 뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }


    // 뷰 데이터 연결
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderId = orderIdList[position]
        val orderData = orderDataMap[orderId]

        // 주문 날짜(yyyy/mm/dd 형태)
        val formatDate =
            "${orderId.substring(0, 4)}/${orderId.substring(4, 6)}/${orderId.substring(6, 8)}"
        holder.orderDate.text = "$formatDate"

        // 조리 상태
        val complete = orderViewModel.finalComplete.value?.get(orderId) ?: false
        if (complete) {
            holder.complete.text = "[ 조리 완료 ]"
            holder.complete.setTextColor(Color.BLACK)
        } else {
            holder.complete.text = "[ 조리 중 ]"
            holder.complete.setTextColor(Color.RED)
        }

        // 첫 번째 가게, 메뉴 안내
        var firstMenuName = ""
        if (!orderData?.storeMap.isNullOrEmpty()) {
            val storeOrder = orderData!!.storeMap
            // 첫 번째 가게명
            val firstStoreName = storeOrder?.keys?.first()
            // 첫 번째 메뉴명
            val firstStoreMenu = storeOrder?.get(firstStoreName)?.menuList?.get(0)?.menu ?: ""
            firstMenuName = firstStoreMenu

            // 가게 수
            val storeAmount = storeOrder?.size
            if (storeAmount != null) {
                holder.orderStore.text = if (storeAmount > 1) {
                    "$firstStoreName 외 ${storeAmount - 1}개"
                } else {
                    firstStoreName
                }
            }

            // 가게 이미지
            when (firstStoreName) {
                "버거운버거" -> holder.storeImage.setImageResource(R.drawable.burger_image)
                "덮밥" -> holder.storeImage.setImageResource(R.drawable.kakao_logo)
                "태산김치찜" -> holder.storeImage.setImageResource(R.drawable.naver_logo)
                "숑숑돈가스" -> holder.storeImage.setImageResource(R.drawable.toss_logo)
                else -> Log.e(tag, "존재하지 않는 store 데이터")
            }
        } else {
            Log.e(tag, "StoreData의 데이터가 확인되지 않음")
        }

        // 메뉴 수
        val totalAmount = orderViewModel.totalAmount.value?.get(orderId) ?: 0
        holder.orderMenu.text = if (totalAmount > 1) {
            "$firstMenuName 외 ${totalAmount - 1}개"
        } else firstMenuName

        // 가격
        val formatPrice = DecimalFormat("#,###")
        val totalPrice = orderViewModel.totalPrice.value?.get(orderId) ?: 0
        if (totalPrice is Number) {
            holder.totalPrice.text = "${formatPrice.format(totalPrice)}원"
        } else {
            holder.totalPrice.text = "가격 정보 없음"
        }
        // 전달 데이터
        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrderDetailCompleteActivity::class.java).apply {
                putExtra("user_id", userId)
                putExtra("order_id", orderId)
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return orderIdList.size
    }
}