package com.example.nsu_alarmy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nsu_alarm.nsu_alarming.databinding.ActivityMenuBinding

// 오늘의 메뉴
import java.time.LocalDate
import com.nsu_alarm.nsu_alarming.databinding.BtnDateBinding

class TodayMenuActivity : AppCompatActivity() {
    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding
    private var selectedButtonIndex = -1 // 선택된 버튼의 인덱스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 날짜 데이터
        val currentTime = LocalDate.now()
        val currentDayOfWeek = currentTime.dayOfWeek.value

        binding.btnMon.dateText.text = getString(R.string.Monday)
        binding.btnTue.dateText.text = getString(R.string.Tuesday)
        binding.btnWed.dateText.text = getString(R.string.Wednesday)
        binding.btnThu.dateText.text = getString(R.string.Thursday)
        binding.btnFri.dateText.text = getString(R.string.Friday)




        val dateButtons =
            listOf(binding.btnMon, binding.btnTue, binding.btnWed, binding.btnThu, binding.btnFri)
                .map { BtnDateBinding.bind(it.root) }


        // 오늘 요일에 맞는 버튼을 선택 상태로 변경
        selectTodayDateBtn(currentDayOfWeek,dateButtons)

        // 버튼을 동적으로 추가, 클릭 이벤트 설정
        dateButtons.forEachIndexed { index, dateBtnBinding ->
            // 클릭 이벤트
            dateBtnBinding.root.setOnClickListener {
                // 클릭된 버튼의 인덱스 저장
                selectedButtonIndex = index
                // 클릭된 버튼만 선택 상태로 설정
                updatedBtnStates(dateBtnBinding, dateButtons)
            }
            // 버튼 레이아웃 추가
            binding.date.addView(dateBtnBinding.root)
        }



        // 화살표 클릭시 이동
        binding.btnPreviousMenu.setOnClickListener {
            if (selectedButtonIndex != -1) {
                // 선택된 버튼 인덱스를 기준으로 전 버튼을 선택
                selectedButtonIndex = if (selectedButtonIndex == 0) dateButtons.size - 1 else selectedButtonIndex - 1
                updatedBtnStates(dateButtons[selectedButtonIndex], dateButtons)
            }
        }

        binding.btnNextMenu.setOnClickListener {
            if (selectedButtonIndex != -1) {
                // 선택된 버튼 인덱스를 기준으로 다음 버튼을 선택
                selectedButtonIndex = (selectedButtonIndex + 1) % dateButtons.size
                updatedBtnStates(dateButtons[selectedButtonIndex], dateButtons)
            }
        }

    }

    //  하단바
    fun setBottomBar() {

    }

    //  오늘의 메뉴
    //모두 선택 해제
    private fun updatedBtnStates(selectBtnDateBinding: BtnDateBinding, dateButtons: List<BtnDateBinding>) {
        dateButtons.forEach { dateBtnBinding ->
            dateBtnBinding.isSelected = false
        }
        selectBtnDateBinding.isSelected = true
    }

    // 오늘 요일에 맞는 버튼을 선택 상태로 변경
    private fun selectTodayDateBtn(TodayDate: Int, dateButtons: List<BtnDateBinding>){
        dateButtons.forEachIndexed { index, dataBtnBinding ->
            dataBtnBinding.isSelected = (TodayDate == index+1)
        }
    }

    // 선택된 날짜에 해당하는 메뉴 출력
//    fun showMenu(){
//        when (currentDayOfWeek) {
//            1 -> {
//
//            } // 월
//            2 -> {
//
//            } // 화
//            3 -> {
//
//            } // 수
//            4 -> {
//
//            } // 목
//            5 -> {
//
//            } // 금
//            else -> {}
//
//        }
//    }

}