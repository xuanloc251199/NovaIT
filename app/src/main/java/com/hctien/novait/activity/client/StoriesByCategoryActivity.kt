package com.hctien.novait.activity.client

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.adapter.StoryAdapter
import com.hctien.novait.data.DBHelper
import com.hctien.novait.Category
import com.hctien.novait.Story
import com.hctien.novait.activity.StoryDetailActivity

class StoriesByCategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DBHelper
    private lateinit var categoryTextView: TextView
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories_by_category)

        // Kiểm tra và lấy các view bằng findViewById
        recyclerView = findViewById(R.id.recyclerViewStoriesByCategory)
        categoryTextView = findViewById(R.id.categoryTextView)

        // Đảm bảo rằng các view đã được khởi tạo thành công
        if (recyclerView == null || categoryTextView == null) {
            Toast.makeText(this, "Lỗi trong việc lấy các view!", Toast.LENGTH_SHORT).show()
            return
        }

        dbHelper = DBHelper(this)

        // Lấy thể loại từ Intent
        val categoryId = intent.getIntExtra("categoryId", -1)

        // Kiểm tra categoryId hợp lệ
        if (categoryId != -1) {
            // Lấy thông tin thể loại từ cơ sở dữ liệu
            val category = dbHelper.getCategoryById(categoryId)

            // Hiển thị tên thể loại
            categoryTextView.text = "Thể loại: ${category?.categoryName}"

            // Lấy tất cả truyện theo categoryId
            val stories = dbHelper.getStoriesByCategoryId(categoryId)

            // Kiểm tra xem có truyện hay không
            if (stories.isNotEmpty()) {
                // Hiển thị truyện trong RecyclerView
                storyAdapter = StoryAdapter(
                    stories = stories,
                    categories = listOf(category!!),
                    dbHelper = dbHelper
                ) { story ->
                    val intent = Intent(this, StoryDetailActivity::class.java)
                    intent.putExtra("storyId", story.id)
                    startActivity(intent)
                }
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = storyAdapter
            } else {
                // Nếu không có truyện, hiển thị thông báo
                Toast.makeText(this, "Không có truyện trong thể loại này", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Lỗi thể loại không hợp lệ", Toast.LENGTH_SHORT).show()
        }
    }
}
