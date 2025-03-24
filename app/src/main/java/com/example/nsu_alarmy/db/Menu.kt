package com.example.nsu_alarmy.db

import androidx.annotation.DrawableRes

data class Menu(
    val id: String,
    val description: String?,
    val price: Int,
    val ingredient: List<String>?,
    @DrawableRes
    val image: Int?
)
