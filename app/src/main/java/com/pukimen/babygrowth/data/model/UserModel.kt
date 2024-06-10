package com.pukimen.babygrowth.data.model

import java.util.Date

data class UserModel(
    val email: String,
    val name: String,
    val birthDay: String,
    val height: Int,
    val weight: Int,
    val gender: String,
    val updatedAt: String,
    val token: String,
    val isLogin: Boolean = false
)