package com.hctien.novait.activity.client

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.R
import com.hctien.novait.model.Chapter

class ChapterActivity : AppCompatActivity() {

    private lateinit var chapterTitleTextView: TextView
    private lateinit var chapterContentTextView: TextView
    private lateinit var chapter: Chapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        // Lấy các View từ layout
        chapterTitleTextView = findViewById(R.id.chapterTitle)
        chapterContentTextView = findViewById(R.id.chapterContent)

        // Lấy thông tin từ Intent
        chapter = intent.getSerializableExtra("chapter") as Chapter

        // Hiển thị thông tin chương
        chapterTitleTextView.text = chapter.title
        chapterContentTextView.text = chapter.content
    }
}
