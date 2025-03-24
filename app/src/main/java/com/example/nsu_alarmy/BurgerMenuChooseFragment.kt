package com.example.nsu_alarmy

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.nsu_alarmy.databinding.BurgerMenuChooseBinding

class BurgerMenuChooseFragment : DialogFragment() {
    private val Tag: String = "Burger Menu Choose"
    private var _binding: BurgerMenuChooseBinding? = null
    private val binding get() = _binding!!

    // 데이터 가져오기
//    private val MenuDetailViewModel by activityViewModels<MenuDetailViewModel> {
//        MenuDetailViewModelFactory(requireContext())
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = BurgerMenuChooseBinding.inflate(LayoutInflater.from(context))

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

        // [단품] 버튼
        binding.btnMenuSingle.setOnClickListener {
            Toast.makeText(dialog.context, "Single 버튼 클릭", Toast.LENGTH_SHORT).show()
            Log.i(Tag, "Single 버튼 클릭")
            BurgerMenuSingleFragment().show(parentFragmentManager, "Burger Single Menu Choose")

            dismiss()
        }
        // [세트] 버튼
        binding.btnMenuSet.setOnClickListener {
            Toast.makeText(dialog.context, "Set 버튼 클릭", Toast.LENGTH_SHORT).show()
            Log.i(Tag, "Set 버튼 클릭")
            BurgerMenuSetFragment().show(parentFragmentManager,"Burger Set Menu Choose")

            dismiss()
        }

        return dialog
    }
}

