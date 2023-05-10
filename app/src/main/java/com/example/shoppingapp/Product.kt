package com.example.shoppingapp

data class Product(
    val name:String,
    val category: String,
    val Description:String,
    val price:String,
    val imageUrl:String,
    val rating:Double,
    val count:Int,
    var likeStatus:Int=0,
    var cart:Int=0
) {

}
