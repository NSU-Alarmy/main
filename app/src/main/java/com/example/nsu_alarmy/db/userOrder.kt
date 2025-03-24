package com.example.nsu_alarmy.db

data class userOrderBurger (
    val userId : String,

    val store : String,
    val menuList: String,
    val menuId : String,
    val set : Boolean?,

    // Opt1
    val cheese :Boolean?,
    val union : Boolean?,
    val lettuce : Boolean?,
    val pickle : Boolean?,
    val tomato : Boolean?,

    val opt2 : String?,

    val num : Int
)