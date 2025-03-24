package com.example.nsu_alarmy

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsu_alarmy.databinding.ActivityBurgerMenuListBinding
import com.example.nsu_alarmy.db.Menu
import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.app
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

const val MENU_NAME = "menu name"

class MenuListActivity : AppCompatActivity() {
    private val Tag = "MenuListActivity"

    private val ListViewModel by viewModels<ListViewModel> {
        ListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityBurgerMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /* DB */
        Log.i(Tag, "데이터 가져오기 시도")
        // DB 인스턴스 초기화
        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        // 데이터 가져오기
        val menuRef = db.collection("store").document("hamburger")
            .collection("burger").document("kEkvhSgLG0fdKpwCK3ud")


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


        /* RecyclerView */
        // 데이터 연결, 각 메뉴 버튼 생성
        val menuAdapter = MenuAdapter { menu -> adapterOnClick(menu) }
        // GridView 데이터 출력 _이미지, 메뉴명
        val recyclerView: RecyclerView = findViewById(R.id.menu_recycle_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = menuAdapter

        ListViewModel.menuLiveData.observe(this, {
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
    private fun adapterOnClick(menu: Menu) {
        Log.i(Tag, "메뉴 선택")
        BurgerMenuChooseFragment().show(supportFragmentManager, "BURGER MENU CHOOSE")
    }

    /* DB */
//    private fun getData(data: Menu) {
//        FirebaseFirestore.getInstance()
//            .collection("store/hamburger/burger")
//            .document(data.name)
//            .get(data)
//    }

}