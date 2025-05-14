package com.hctien.novait.activity.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.adapter.StatisticAdapter
import com.hctien.novait.data.DBHelper
import com.hctien.novait.model.Statistic

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val dbHelper = DBHelper(this)

        // Lấy dữ liệu thống kê từ DB
        val statistics = listOf(
            Statistic("Tổng số truyện", dbHelper.getStoryCount().toString()),
            Statistic("Tổng số chương", dbHelper.getChapterCount().toString()),
            Statistic("Tổng số lượt xem", dbHelper.getTotalViewCount().toString()),
            Statistic("Tổng số bình luận", dbHelper.getCommentCount().toString()),
            Statistic("Tổng số người dùng", dbHelper.getUserCount().toString())
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStatistics)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = StatisticAdapter(statistics)
        recyclerView.adapter = adapter
    }
}

