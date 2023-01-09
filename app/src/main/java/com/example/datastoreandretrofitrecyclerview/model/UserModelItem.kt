package com.example.datastoreandretrofitrecyclerview.model

data class UserModelItem(
    val id: Int,
    val title: String,
    val body: String,
    var isSaved : Boolean = false,
)