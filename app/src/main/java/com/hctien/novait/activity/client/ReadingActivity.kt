package com.hctien.novait.activity.client

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.R

class ReadingActivity : AppCompatActivity() {

    private lateinit var txtContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)

        txtContent = findViewById(R.id.txtContent)

        val content = intent.getStringExtra("content")
        txtContent.text = content ?: "Không có nội dung."
    }
}
