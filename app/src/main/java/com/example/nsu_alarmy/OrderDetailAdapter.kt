package com.example.nsu_alarmy

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsu_alarmy.data.OrderViewModel
import com.example.nsu_alarmy.data.StoreOrder

class OrderDetailAdapter(
    private val context: Context,
    orderViewModel: OrderViewModel,
    lifecycleOwner: LifecycleOwner,

    ) : RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {
    private val tag = "OrderDetailAdapter"

    private var storeList: List<Pair<String, StoreOrder>> = listOf()

    /* 어댑터 초기화 */
    init {
        orderViewModel.orderDataByOrderId.observe(lifecycleOwner) { data ->
            storeList = data.storeMap?.toList() ?: listOf()
            Log.d(tag, "데이터 갱신: $data")

            notifyDataSetChanged()
        }
    }


    /* RecyclerView 형태 설정 */
    // activity_order_detail_complete.xml 형태로 뷰 생성
    class OrderDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeName: TextView = view.findViewById(R.id.storeTextView)
        val storeComplete: TextView = view.findViewById(R.id.orderStatusTextView)
        val storeOrderRecyclerView: RecyclerView = view.findViewById(R.id.rv_menu_each_store)
    }

    // 뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_order_detail_menu, parent, false)
        return OrderDetailViewHolder(view)
    }

    // 뷰 데이터 연결
    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val (storeName, storeOrder) = storeList[position]
        // 가게명
        holder.storeName.text = storeName
        // 조리 상태
        if (storeOrder.complete) {
            holder.storeComplete.text = "[ 조리 완료 ]"
            holder.storeComplete.setTextColor(Color.BLUE)
        } else {
            holder.storeComplete.text = "[ 조리 중 ]"
            holder.storeComplete.setTextColor(Color.RED)
        }

        /* 내부 recyclerView 설정 */
        // recyclerView 사이즈 조절
        val orderStoreAdapter = OrderStoreAdapter(context)
        holder.storeOrderRecyclerView.setHasFixedSize(false)
        holder.storeOrderRecyclerView.isNestedScrollingEnabled = false
        // recyclerView 형태 조절
        holder.storeOrderRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.storeOrderRecyclerView.adapter = orderStoreAdapter

        // recyclerView(가게별 메뉴)에 데이터 전달
        orderStoreAdapter.submitList(storeOrder.menuList ?: listOf())

    }

    override fun getItemCount(): Int {
        return storeList.size
    }
}