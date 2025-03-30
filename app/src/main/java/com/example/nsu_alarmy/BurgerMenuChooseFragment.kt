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
import androidx.fragment.app.viewModels
import com.example.nsu_alarmy.databinding.BurgerMenuChooseBinding

class BurgerMenuChooseFragment : DialogFragment() {
    private val Tag: String = "Burger Menu Choose"

    private var _binding: BurgerMenuChooseBinding? = null
    private val binding get() = _binding!!

    // 데이터 가져오기
    private val menuListViewModel by viewModels<MenuDetailViewModel> {
        MenuDetailViewModelFactory(requireActivity())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = BurgerMenuChooseBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        val menuName: TextView = binding.burgerName
        val menuDescription: TextView = binding.burgerDescription
        val menuIngredient: TextView = binding.burgerIngredient

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


        // 선택한 메뉴명
        val currentMenuId: String? = arguments?.getString("Menu Id")
        // Menu 상세 데이터 가져오기
        if (currentMenuId != null) {
            currentMenuId.let {
                val currentMenu = menuListViewModel.getMenuDetailForId(currentMenuId)
                menuName.text = currentMenu?.id
                menuDescription.text = currentMenu?.description
                menuIngredient.text = currentMenu?.ingredient.toString()
            }
        } else Log.i(Tag, "선택된 메뉴에 대한 정보 없음")


        // [단품] 버튼
        binding.btnMenuSingle.setOnClickListener {
            Log.i(Tag, "Single 버튼 클릭")
            var bundle = Bundle()
            bundle.putString("Menu Id", currentMenuId)
            val fragment = BurgerMenuSingleFragment()
            fragment.arguments = bundle
            fragment.show(parentFragmentManager, "Burger Single Menu Choose")
        }
        // [세트] 버튼
        binding.btnMenuSet.setOnClickListener {
            Log.i(Tag, "Set 버튼 클릭")
            var bundle = Bundle()
            bundle.putString("Menu Id", currentMenuId)
            val fragment = BurgerMenuSetFragment()
            fragment.arguments = bundle
            fragment.show(parentFragmentManager, "Burger Set Menu Choose")
        }

        return dialog
    }
}

