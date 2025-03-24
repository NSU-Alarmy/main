package com.example.nsu_alarmy.db

import android.content.res.Resources
import com.example.nsu_alarmy.R

fun chickenList(resources: Resources):List<Menu>{
    return listOf(
        Menu(
            id = "후라이드 치킨",
            price = 13000,
            description = "바삭바삭 맛있는 치킨",
            ingredient = listOf("닭","후추"),
            image = R.drawable.burger_image
        ),
        Menu(
            id = "양념 치킨",
            price = 15000,
            description = "매콤한 치킨",
            ingredient = listOf("닭","우유"),
            image = R.drawable.burger_image
        )
    )

}