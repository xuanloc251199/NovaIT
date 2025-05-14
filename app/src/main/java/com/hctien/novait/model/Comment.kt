package com.hctien.novait.model

import java.io.Serializable

data class Comment(
    val id: Int,
    val storyId: Int,
    val userId: Int,
    val username: String, // tên tài khoản
    val displayName: String, // tên hiển thị
    val text: String,
    val timestamp: Long
) : Serializable
