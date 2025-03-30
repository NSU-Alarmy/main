package com.example.nsu_alarmy

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nsu_alarmy.databinding.ActivityBurgerListBinding
import com.example.nsu_alarmy.db.sideList

class BurgerListActivity : AppCompatActivity() {
    private val Tag: String = "BurgerListActivity"
    private lateinit var binding: ActivityBurgerListBinding
    var listChoose: String = "menuList" // 선택한 menuList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBurgerListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = Intent(this, MenuListActivity::class.java)
        // 버거 Menu List로 이동
        binding.btnListBurger.setOnClickListener {
            Log.i(Tag, "햄버거 선택")
            listChoose = "burgerList"
            startActivity(intent)
        }

        // 치킨 Menu List로 이동
        binding.btnListChicken.setOnClickListener {
            Log.i(Tag, "치킨 선택")
            listChoose = "chickenList"
            startActivity(intent)
        }

        // 사이드 Menu List로 이동
        binding.btnListSide.setOnClickListener {
            Log.i(Tag, "사이드 선택")
            listChoose = "sideList"
            startActivity(intent)
        }


        // 이전 화면으로 가기_Activity Burger Main
        binding.btnPrevious.setOnClickListener {
            val intentPrevious = Intent(this, BurgerMainActivity::class.java)
            startActivity(intentPrevious)
        }
    }
    
    // 선택한 버튼의 데이터를 가져와 리스트로 저장

}