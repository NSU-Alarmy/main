package com.example.nsu_alarmy.db

import android.content.res.Resources
import com.example.nsu_alarmy.R

fun burgerList(resource: Resources):List<Menu>{
    return listOf(
        Menu(
            id = "새우버거",
            description = "오동동한 새우가 들어간 맛있는 버거",
            price = 4000,
            ingredient = listOf("새우","밀"),
            image = null,
        ),
        Menu(
            id="불고기버거",
            description = "맛있는 불고기가 들어간 베스트 메뉴",
            price = 5000,
            ingredient = listOf("소고기","밀"),
            image = R.drawable.burger_image
        ),
        Menu(
            id = "싸이버거",
            description = "한입에 넣기 어려울만큼 큰 치킨이 들어간 버거",
            price = 7000,
            ingredient = listOf("닭","밀"),
            image = R.drawable.burger_image
        )
    )
}