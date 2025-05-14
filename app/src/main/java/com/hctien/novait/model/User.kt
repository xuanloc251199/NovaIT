package com.hctien.novait

data class User(
    val id: Int = 0,
    val username: String,
    val password: String,
    val role: String,
    val displayName: String
)
