package com.example.nsu_alarmy.db

import android.content.res.Resources
import com.example.nsu_alarmy.R

fun sideList(resources: Resources): List<Menu> {
    return listOf(
        Menu(
            id = "사이드 1",
            price = 1500,
            description = null,
            ingredient = listOf("감자"),
            image = R.drawable.burger_image
        )
    )
}