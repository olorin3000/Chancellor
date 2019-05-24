package com.chancellor.api

data class DefaultResponse<T> (
    val success: Boolean = true,
    var data : T? = null
)