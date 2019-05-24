package com.chancellor.api

data class User (
    val userId: Int,
    val phone: String,
    val email: String,
    val firstName: String,
    val secondName: String,
    val address: String,
    val password: String
)