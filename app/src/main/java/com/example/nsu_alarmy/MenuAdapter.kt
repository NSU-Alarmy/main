/* RecyclerView에 있는 요소의 모양, 동작 방식 설계 */
// ViewHolder : 각 요소의 모양/동작 방식 설계
// ViewAdapter : 데이터를 ViewHolder와 연결
package com.example.nsu_alarmy;

import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.nsu_alarmy.db.Menu

import kotlin.Unit;

class MenuAdapter(private val onClick: (Menu) -> Unit) :
    ListAdapter<Menu, MenuAdapter.MenuViewHolder>(MenuDiffCallback) {

    // 개별 요소 관리
    class MenuViewHolder(itemView: View, val onClick: (Menu) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val menuTextView: TextView = itemView.findViewById(R.id.menu_name)
        private val menuImageView: ImageView = itemView.findViewById(R.id.menu_image)
        private var currentMenu: Menu? = null

        // 객체 초기화
        init {
            itemView.setOnClickListener {
                currentMenu?.let {
                    onClick(it)
                }
            }
        }

        // ViewHolder에 'Menu'데이터 설정
        fun bind(menu: Menu) {
            currentMenu = menu

            // 이름 설정
            menuTextView.text = menu.id
            // 이미지 설정
            if (menu.image != null) {
                menuImageView.setImageResource(menu.image)
            } else {
                menuImageView.setImageResource(R.drawable.burger_store_image)
            }
        }
    }

    // viewHolder 새로 생성 _ xml을 ViewHolder로 변환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.burger_menu, parent, false)
        return MenuViewHolder(view, onClick)
    }

    // 데이터와 ViewHolder 연결
    override fun onBindViewHolder(holder: MenuViewHolder, postion: Int) {
        val menu = getItem(postion)
        holder.bind(menu)
    }

    // 리스트 변경 감지하여 업데이트
    object MenuDiffCallback : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
