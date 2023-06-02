package com.cp23ps480.rubist.Model

data class UserModel(
    val uid: String,
    //val name: String,
    val isLogin: Boolean,
    val token: String = ""
)
