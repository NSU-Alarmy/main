package com.example.nsu_alarmy

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.nsu_alarmy.databinding.BurgerMenuSingleBinding

class BurgerMenuSingleFragment : DialogFragment() {
    private val Tag: String = "BurgerMenuSingleFragment"
    private var _binding: BurgerMenuSingleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = BurgerMenuSingleBinding.inflate(LayoutInflater.from(context))

        val menuName: TextView = binding.burgerName
        val menuDescription: TextView = binding.burgerDescription
        val menuIngredient: TextView = binding.burgerIngredient

        dialog.setContentView(binding.root)

        // 크기 조정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        // 바깥 영역 어둡기 조절
        dialog.window?.setDimAmount(0.7f)

        // 바깥 영역 or 뒤로가기 버튼 클릭 시,  취소
        isCancelable = true
        dialog.setCancelable(true)

        // 이미지 순서 변경
        binding.pinImage.bringToFront()

        dialog.show()

        return dialog
    }
}