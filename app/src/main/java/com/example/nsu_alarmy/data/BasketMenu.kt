package com.example.nsu_alarmy.data

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class BasketMenu(
    @DocumentId
    val id: String = "",
    val store: String = "",
    val menu: String = "",
    val menuPrice: Int = 0,
    val amount: Int = 1,
    val optionList: List<Option>? = null
) : Serializable

data class Option(
    val optionName: String = "",
    val optionPrice: Int = 0
) : Serializable