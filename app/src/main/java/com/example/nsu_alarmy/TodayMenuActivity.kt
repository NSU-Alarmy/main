package com.example.nsu_alarmy

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import com.example.nsu_alarmy.databinding.ActivityTodaymenuBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// 오늘의 메뉴
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import com.example.nsu_alarmy.db.TodayMenu


class TodayMenuActivity : AppCompatActivity() {
    private val tag = "TodayMenuActivity"
    private lateinit var binding: ActivityTodaymenuBinding

    private lateinit var weekMenuData: List<TodayMenu>

    /* 오늘 날짜 */
    private val currentTime: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))

    // 오늘 요일
    private val currentDayOfWeek = currentTime.dayOfWeek.value
    // 해당 월
    private val currentMonth = currentTime.month.value
    // 해당 주차
    private val calendar: Calendar = Calendar.getInstance(Locale.KOREA)
    private val currentWeek = calendar.get(Calendar.WEEK_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTodaymenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /* 오늘 날짜 */
        Log.i(tag, "현재 날짜 : $currentTime")
        // 오늘 요일
        Log.i(tag, "요일: $currentDayOfWeek")
        // 해당 월
        Log.i(tag, "월: $currentMonth")
        // 해당 주차
        Log.i(tag, "주차: $currentWeek")


        /* '오늘의 메뉴' 데이터 가져오기 */
        loadWeeklyMenu { menuData ->
            weekMenuData = menuData

            if (weekMenuData.size > 1) {
                Log.i(tag, "데이터 가져오기 완료")
                showTodayMenu(currentDayOfWeek)
            } else {
                Log.i(tag, "데이터 가져오기 실패")
            }
        }


        /* 날짜 버튼 데이터 */
        // 날짜 버튼에 데이터 입력
        // 날짜 요일값
        binding.btnMon.btnDateText = ObservableField(getString(R.string.Monday))
        binding.btnTue.btnDateText = ObservableField(getString(R.string.Tuesday))
        binding.btnWed.btnDateText = ObservableField(getString(R.string.Wednesday))
        binding.btnThu.btnDateText = ObservableField(getString(R.string.Thursday))
        binding.btnFri.btnDateText = ObservableField(getString(R.string.Friday))

        // 날짜 숫자값
        binding.btnMon.btnDateNum = getDateNum(1)
        binding.btnTue.btnDateNum = getDateNum(2)
        binding.btnWed.btnDateNum = getDateNum(3)
        binding.btnThu.btnDateNum = getDateNum(4)
        binding.btnFri.btnDateNum = getDateNum(5)

        // 날짜 버튼 상태
        val mon = binding.btnMon.root
        val tue = binding.btnTue.root
        val wed = binding.btnWed.root
        val thu = binding.btnThu.root
        val fri = binding.btnFri.root

        val dateButtons = mapOf(
            1 to mon,
            2 to tue,
            3 to wed,
            4 to thu,
            5 to fri
        )

        // 오늘 날짜 선택
        selectBtnState(currentDayOfWeek, dateButtons)

        // 날짜 버튼 클릭 시
        activateBtn(1, dateButtons)
        activateBtn(2, dateButtons)
        activateBtn(3, dateButtons)
        activateBtn(4, dateButtons)
        activateBtn(5, dateButtons)


        /* 화살표 버튼 */
        // 선택한 날짜(num) 받기
        var selectedDay: Int? = dateButtons.entries.find { it.value.isSelected }?.key
        // 화살표 클릭시 이동
        binding.btnPreviousMenu.setOnClickListener {
            Log.i(tag, "이전버튼 클릭")
            selectedDay = if (selectedDay == 1) {
                5
            } else {
                selectedDay?.minus(1)
            }
            selectBtnState(selectedDay!!, dateButtons)
            showTodayMenu(selectedDay!!)
        }

        binding.btnNextMenu.setOnClickListener {
            Log.i(tag, "다음버튼 클릭")
            selectedDay = if (selectedDay == 5) {
                1
            } else {
                selectedDay?.plus(1)
            }
            selectBtnState(selectedDay!!, dateButtons)
            showTodayMenu(selectedDay!!)
        }

    }

    //  하단바
    fun setBottomBar() {}


    /* '오늘의 메뉴' 데이터 가져오기 */
    private fun loadWeeklyMenu(callback: (List<TodayMenu>) -> Unit) {
        Log.i(tag, "FireStore에서 데이터 가져오기")
        // FireStore 연결
        val db = Firebase.firestore

        val todayWeek = "${currentMonth}th_${currentWeek}week_menu"
        val docRef = db.collection("today's_menu").document(todayWeek)

        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")

        docRef.get()
            .addOnSuccessListener { document ->
                // 월~금 메뉴 리스트
                val weekMenuList = mutableListOf<TodayMenu>()

                if (document != null && document.exists()) {
                    // 월~금 오늘의 메뉴 가져오기
                    for ((index, day) in days.withIndex()) {
                        val rawMenuString = document.getString(day)

                        // 월~금 오늘의 메뉴 저장
                        if (!rawMenuString.isNullOrBlank()) {
                            val todayMenuArray = rawMenuString.split(",").map { it.trim() }

                            val todayMenu = TodayMenu(
                                id = day,
                                dayOfWeek = index + 1,
                                menu = todayMenuArray.joinToString("\n\n")
                            )
                            weekMenuList.add(todayMenu)
                        }
                    }
                } else {
                    Log.i(tag, "문서가 존재하지 않음")
                }
                callback(weekMenuList)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore Error", "데이터 불러오기 실패", e)
            }
    }

    // 날짜(num) 구하기
    private fun getDateNum(day: Int): Int {
        val getDate = currentTime.plusDays((day - currentDayOfWeek).toLong())
        val dayNum: Int = getDate.dayOfMonth
        return dayNum
    }

    // 선택한 버튼 활성화
    private fun selectBtnState(day: Int, testButtons: Map<Int, View>) {
        for ((key, button) in testButtons) {
            // day와 일치하는 요일을 가질 시 true로 반환 -> 선택
            button.isSelected = (key == day)
        }
    }

    // 날짜에 해당하는 메뉴 리스트 가져오기
    private fun showTodayMenu(day: Int) {
        val selected = weekMenuData.find { it.dayOfWeek == day }
        binding.menuBox.text = selected?.menu ?: "메뉴 정보 없음"
    }

    // 선택한 날짜 버튼 활성화, 메뉴 출력
    private fun activateBtn(day: Int, dateButtons: Map<Int, View>) {
        Log.i(tag, "activateBtn 호출:$day")
        dateButtons[day]?.setOnClickListener {
            selectBtnState(day, dateButtons)
            showTodayMenu(day)
        }
    }

}
