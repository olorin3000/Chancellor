package com.chancellor.api

data class ErrorResponse(
    val success: Boolean = false,
    val message: String
)