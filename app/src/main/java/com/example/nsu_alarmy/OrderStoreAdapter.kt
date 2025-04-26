package com.example.nsu_alarmy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nsu_alarmy.data.BasketMenu
import com.example.nsu_alarmy.data.OrderMenu
import com.example.nsu_alarmy.data.OrderViewModel

class OrderStoreAdapter(
    private val context: Context,
    private val orderViewModel: OrderViewModel
) : ListAdapter<OrderMenu, OrderStoreAdapter.OrderStoreViewHolder> {


    /* 어댑터 초기화 */
    inner class OrderStoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    /* RecyclerView 형태 설정 */
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): OrderStoreAdapter.OrderStoreViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_receipt, viewGroup, false)

    }


    override fun onBindViewHolder(holder: OrderStoreAdapter.OrderStoreViewHolder, position: Int) {

    }
}