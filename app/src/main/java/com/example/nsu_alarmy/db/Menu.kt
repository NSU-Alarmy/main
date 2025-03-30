/* 전달받을 데이터 형태 */
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

//class getMenu constructor(
//    val name: String,
//    val price: Int,
//    val description: String?,
//    val ingredient: List<String>?,
//    val image: Int?
//) {
//    init {
//        if(name.isEmpty()){
//            throw IllegalArgumentException("${name} 메뉴 없음")
//        }
//        this.name
//        this.price
//        this.description
//        this.ingredient
//        this.image
//    }
//
//}
