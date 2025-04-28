package com.example.nsu_alarmy

import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nsu_alarmy.data.OrderMenu
import com.example.nsu_alarmy.data.OrderViewModel

class OrderStoreAdapter(
    private val context: Context,
    private var orderMenuList: List<OrderMenu> = emptyList()
) : RecyclerView.Adapter<OrderStoreAdapter.OrderStoreViewHolder>() {

    class OrderStoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuName: TextView = view.findViewById(R.id.menu_name_textview)
        val totalPrice: TextView = view.findViewById(R.id.total_price_textview)
        val menuPrice: TextView = view.findViewById(R.id.menu_price_textview)
        val menuAmount: TextView = view.findViewById(R.id.menu_amount_textView)
        val optionContainer: LinearLayout = view.findViewById(R.id.option_container)
        val optionText: TextView = view.findViewById(R.id.add_option)
    }

    /* RecyclerView 형태 설정 */
    // 뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderStoreViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_order_menu, parent, false)
        return OrderStoreViewHolder(view)
    }

    // 뷰 데이터 연결
    override fun onBindViewHolder(holder: OrderStoreViewHolder, position: Int) {
        val orderMenu = orderMenuList[position]
        val options = orderMenu.optionList
        // 메뉴명
        holder.menuName.text = orderMenu.menu
        // 메뉴 가격
        holder.menuPrice.text = "${orderMenu.menuPrice}원"
        // 메뉴 개수
        holder.menuAmount.text = "${orderMenu.amount}개"
        // 총 금액
        val formatPrice = DecimalFormat("#,###")
        var totalMenuPrice = orderMenu.menuPrice
        options?.forEach { option ->
            totalMenuPrice += option.optionPrice
        }
        totalMenuPrice *= orderMenu.amount
        holder.totalPrice.text = "${formatPrice.format(totalMenuPrice)}원"

        // 하단에 옵션 정보 추가
        val optionContainer = holder.optionContainer
        optionContainer.removeAllViews()

        if (options != null && options.isNotEmpty()) {
            holder.optionText.visibility = View.VISIBLE // 옵션 있으면 '-옵션' 표시
            options?.forEach { option ->
                val optionView = LayoutInflater.from(optionContainer.context)
                    .inflate(R.layout.item_option, optionContainer, false)

                val optionName = optionView.findViewById<TextView>(R.id.option_name_textview)
                val optionPrice = optionView.findViewById<TextView>(R.id.option_price_textview)
                // 옵션명
                optionName.text = " ↳ ${option.optionName}"
                // 옵션 가격
                optionPrice.text = "${formatPrice.format(option.optionPrice)}원"

                optionContainer.addView(optionView)
            }
        } else {
            holder.optionText.visibility = View.GONE // 옵션 없으면 '-옵션' 숨기기
        }
    }

    override fun getItemCount(): Int {
        return orderMenuList.size
    }

    // 데이터 업데이트
    fun submitList(newList: List<OrderMenu>) {
        orderMenuList = newList
        notifyDataSetChanged()
    }

}