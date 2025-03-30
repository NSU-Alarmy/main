package com.example.nsu_alarmy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsu_alarmy.databinding.ActivityBurgerMenuListBinding
import com.example.nsu_alarmy.db.Menu

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class MenuListActivity : AppCompatActivity() {
    private val Tag = "MenuListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityBurgerMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 형태, 기능 가져오기
        val listViewModel by viewModels<ListViewModel> {
            ListViewModelFactory(this)
        }

        /* DB */
        Log.i(Tag, "데이터 가져오기 시도")
        // DB 인스턴스 초기화
        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        // 데이터 가져오기
//        val menuRef = db.collection("store").document("hamburger")
//            .collection("burger").document("kEkvhSgLG0fdKpwCK3ud")

//        menuRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d(TAG, "DocumentSnapshot data :${menuRef.id}")
//                    Log.i(Tag,menuRef.)
//
//
//                } else {
//                    Log.d(TAG, "No Data")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Get Failed with", exception)
//            }
//        menuRef.get().addOnCompleteListener { documents ->
//            for(document in documents){
//                Log.d(TAG, "${document.id} => ${document.data}")
//            }
//        }

        val menuRef = db.collection("store").document("hamburger")
            .collection("burger")
            .get()
            .addOnSuccessListener { result ->
                for (i in result) {
                    val value = i.data.toString()
                    Log.i(Tag, "${i.id}=>${i.data}")
                    //getMenu(i.id, i.data.ingredient)

                }
            }.addOnFailureListener {
                Log.i(Tag, "데이터 가져오기 실패")
            }


        /* RecyclerView */
        // 데이터 연결, 각 메뉴 버튼 생성
        val menuAdapter = MenuAdapter { menu -> adapterOnClick(menu) }
        // GridView 데이터 출력 _이미지, 메뉴명
        val recyclerView: RecyclerView = findViewById(R.id.menu_recycle_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = menuAdapter

        listViewModel.menuLiveData.observe(this, {
            it?.let {
                menuAdapter.submitList(it as MutableList<Menu>)
            }
        })
        Log.i(Tag, "메뉴 리스트 출력 완료")


        // 이전 화면으로 가기_Activity Burger Main
        binding.btnPrevious.setOnClickListener {
            val intentPrevious = Intent(this, BurgerListActivity::class.java)
            startActivity(intentPrevious)
        }
    }

    /* 메뉴 버튼 */
    // 메뉴 클릭시 단품/세트 선택 팝업창 표시
    // 팝업창에 Menu Id값 전달
    private fun adapterOnClick(menu: Menu) {
        var bundle = Bundle()
        bundle.putString("Menu Id", menu.id)
        Log.i(Tag, "메뉴 선택 : ${menu.id}")
        val fragment = BurgerMenuChooseFragment()
        fragment.arguments = bundle
        fragment.show(supportFragmentManager, "BURGER MENU CHOOSE")
    }

    /* DB */
//    private fun getData(data: Menu) {
//        FirebaseFirestore.getInstance()
//            .collection("store/hamburger/burger")
//            .document(data.name)
//            .get(data)
//    }

}