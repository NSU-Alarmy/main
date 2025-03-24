package com.example.nsu_alarmy

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.nsu_alarmy.databinding.BurgerMenuSetBinding

class BurgerMenuSetFragment : DialogFragment() {
    private val Tag: String = "BurgerMenuSetFragment"
    private var _binding: BurgerMenuSetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = BurgerMenuSetBinding.inflate(LayoutInflater.from(context))

        val menuName: TextView = binding.burgerName
        val menuInfo: TextView = binding.burgerDescription
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

        /* 옵션1 버튼 */
        // 0:unchecked, 1:checked
//        var checkOption1:List<CheckBox> =
        binding.checkCheese.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.i(Tag,"Remove Cheese : $isChecked")
        }
        binding.checkUnion.setOnClickListener {

        }
        binding.checkLettuce.setOnClickListener {

        }
        binding.checkPickle.setOnClickListener {

        }
        binding.checkTomato.setOnClickListener {

        }

        /* 옵션2 버튼 */
        // 0:unchecked, 1:checked
//        fun option2:List<CheckBox>=
//        binding.checkDefaultPotato.apply { isChecked(true) }
        binding.checkDefaultPotato.setOnClickListener {

        }
        binding.checkNugget.setOnClickListener {

        }
        binding.checkCheesepotato.setOnClickListener {

        }
        binding.checkRedpotato.setOnClickListener {

        }

        /* 수량 버튼 */
        binding.btnPlus.setOnClickListener {

        }
        binding.btnMinus.setOnClickListener {

        }

        /* 장바구니 버튼 */
        binding.btnAddCart.setOnClickListener {

        }

        dialog.show()

        return dialog

    }

    fun selectOption2(){

    }
}