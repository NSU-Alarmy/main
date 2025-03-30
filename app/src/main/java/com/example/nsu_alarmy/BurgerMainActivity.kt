package com.example.nsu_alarmy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nsu_alarmy.databinding.ActivityBurgerMainBinding

class BurgerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBurgerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBurgerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Burger List 화면으로 이동
        val intent = Intent(this, BurgerListActivity::class.java)
        binding.btnBurgerOrder.setOnClickListener {
            startActivity(intent)
        }

        binding.btnBurgerShadow.setOnClickListener {
            startActivity(intent)
        }


        // 이전 화면으로 가기_Activity Main
        binding.btnPrevious.setOnClickListener{

        }

    }

}