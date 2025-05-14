package com.hctien.novait

data class Story(
    val id: Int = 0,
    val title: String,
    val content: String,
    val author: String,
    val chapterCount: Int,
    var viewCount: Int,
    val rating: Float,
    val categoryId: Int,
    val status: String,
    val coverImageUrl: String
)
