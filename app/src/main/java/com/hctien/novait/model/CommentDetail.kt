package com.hctien.novait.model

data class CommentDetail(
    val id: Int,
    val storyId: Int,
    val storyTitle: String,
    val userId: Int,
    val displayName: String,
    val text: String,
    val timestamp: Long
)
