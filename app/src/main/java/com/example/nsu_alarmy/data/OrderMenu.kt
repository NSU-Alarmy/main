package com.example.nsu_alarmy.data

data class OrderMenu(
    val menu: String = "",
    val menuPrice: Int = 0,
    val amount: Int = 1,
    val optionList: List<Option>? = null,
)