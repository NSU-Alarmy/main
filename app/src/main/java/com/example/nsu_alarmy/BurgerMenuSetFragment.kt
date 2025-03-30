package com.example.nsu_alarmy

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.nsu_alarmy.databinding.BurgerMenuSetBinding
import java.text.NumberFormat
import java.util.Locale

class BurgerMenuSetFragment : DialogFragment() {
    private val Tag: String = "BurgerMenuSetFragment"

    private var _binding: BurgerMenuSetBinding? = null
    private val binding get() = _binding!!

    // 데이터 가져오기
    private val menuListViewModel by viewModels<MenuDetailViewModel> {
        MenuDetailViewModelFactory(requireActivity())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = BurgerMenuSetBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        val menuName: TextView = binding.burgerName
        val menuDescription: TextView = binding.burgerDescription
        val menuIngredient: TextView = binding.burgerIngredient
        val menuPrice: TextView = binding.burgerPrice

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

        var priceNum: Int?

        /* 선택한 메뉴 데이터 가져오기 */
        val currentMenuId: String? = arguments?.getString("Menu Id")
        // Menu 상세 데이터 가져오기
        if (currentMenuId != null) {
            currentMenuId.let {
                val currentMenu = menuListViewModel.getMenuDetailForId(currentMenuId)
                menuName.text = currentMenu?.id
                menuDescription.text = currentMenu?.description
                menuIngredient.text = currentMenu?.ingredient.toString()
                priceNum = currentMenu?.price
                menuPrice.text = NumberFormat.getNumberInstance(Locale.KOREA).format(priceNum)
            }
        } else Log.i(Tag, "선택된 메뉴에 대한 정보 없음")


        /* 옵션1 버튼 */
        // 0:unchecked, 1:checked
        // var checkOption1:List<Menu> =
        binding.checkCheese.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.i(Tag, "Remove Cheese : $isChecked")
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
        // 0:defaultPotato, 1:nugget, 2:cheesePotato, 3:redPotato
        var setOption2: Int = 0
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

    fun selectOption2() {

    }
}