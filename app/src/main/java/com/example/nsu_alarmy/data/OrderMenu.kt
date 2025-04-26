package com.example.nsu_alarmy.data

data class OrderData(
    val orderNum: Int? = 0,
    val payment: String? = "",
    val storeMap: Map<String, StoreOrder>? = emptyMap()
)

data class StoreOrder(
    val complete: Boolean = false,
    val menuList: List<OrderMenu>? = null
)

data class OrderMenu(
    val menu: String = "",
    val menuPrice: Int = 0,
    val amount: Int = 1,
    val optionList: List<Option>? = null,
)