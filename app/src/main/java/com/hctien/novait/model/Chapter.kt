package com.hctien.novait.model

import java.io.Serializable

data class Chapter(
    val id: Int = 0,
    val storyId: Int,  // ID của truyện (khóa ngoại từ bảng stories)
    val title: String,  // Tiêu đề chương
    val content: String  // Nội dung chương
) : Serializable  // Đảm bảo Chapter có thể được truyền qua Intent
