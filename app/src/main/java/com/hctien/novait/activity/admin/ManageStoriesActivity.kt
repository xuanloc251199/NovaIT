package com.hctien.novait.activity.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.adapter.StoryAdminAdapter
import com.hctien.novait.data.DBHelper
import android.content.SharedPreferences
import com.hctien.novait.activity.StoryDetailActivity

class ManageStoriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addStoryButton: Button
    private lateinit var dbHelper: DBHelper
    private lateinit var storyAdminAdapter: StoryAdminAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var userRole: String = "client"  // Mặc định là "client"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_stories)

        recyclerView = findViewById(R.id.recyclerViewStories)
        addStoryButton = findViewById(R.id.addStoryButton)
        dbHelper = DBHelper(this)

        // Lấy vai trò người dùng từ SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        userRole = sharedPreferences.getString("userRole", "client") ?: "client"  // Lấy role người dùng

        // Lấy tất cả thể loại và truyện từ DB
        val categories = dbHelper.getCategories()
        val stories = dbHelper.getAllStories()

        // Hiển thị danh sách truyện trong RecyclerView với adapter dành cho admin
        storyAdminAdapter = StoryAdminAdapter(this, stories, categories, userRole, dbHelper,
            { story -> // Sửa truyện
                val intent = Intent(this, EditStoryActivity::class.java)
                intent.putExtra("storyId", story.id)
                startActivityForResult(intent, REQUEST_CODE_EDIT_STORY)
            },
            { story -> // Xóa truyện
                val result = dbHelper.deleteStory(story.id)
                if (result > 0) {
                    Toast.makeText(this, "Truyện đã bị xóa", Toast.LENGTH_SHORT).show()
                    // Cập nhật lại danh sách truyện trong RecyclerView
                    reloadStories()
                } else {
                    Toast.makeText(this, "Không thể xóa truyện", Toast.LENGTH_SHORT).show()
                }
            },
            { story -> // Sự kiện nhấp vào truyện
                val intent = Intent(this, StoryDetailActivity::class.java)
                intent.putExtra("storyId", story.id)
                startActivity(intent)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storyAdminAdapter

        // Thêm chức năng thêm truyện
        addStoryButton.setOnClickListener {
            val intent = Intent(this, EditStoryActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_STORY)
        }
    }

    // Hàm để reload lại danh sách truyện trong RecyclerView
    private fun reloadStories() {
        // Lấy danh sách thể loại và truyện từ DB
        val categories = dbHelper.getCategories()
        val stories = dbHelper.getAllStories()

        // Cập nhật lại adapter với danh sách truyện mới
        storyAdminAdapter = StoryAdminAdapter(this, stories, categories, userRole, dbHelper,
            { story -> // Sửa truyện
                val intent = Intent(this, EditStoryActivity::class.java)
                intent.putExtra("storyId", story.id)
                startActivityForResult(intent, REQUEST_CODE_EDIT_STORY)
            },
            { story -> // Xóa truyện
                val result = dbHelper.deleteStory(story.id)
                if (result > 0) {
                    Toast.makeText(this, "Truyện đã bị xóa", Toast.LENGTH_SHORT).show()
                    // Cập nhật lại danh sách truyện trong RecyclerView
                    reloadStories()
                } else {
                    Toast.makeText(this, "Không thể xóa truyện", Toast.LENGTH_SHORT).show()
                }
            },
            { story -> // Sự kiện nhấp vào truyện
                val intent = Intent(this, StoryDetailActivity::class.java)
                intent.putExtra("storyId", story.id)
                startActivity(intent)
            }
        )

        // Cập nhật lại RecyclerView với adapter mới
        recyclerView.adapter = storyAdminAdapter
    }

    companion object {
        const val REQUEST_CODE_EDIT_STORY = 1001
        const val REQUEST_CODE_ADD_STORY = 1002
    }

    // Hàm này được gọi khi quay lại từ EditStoryActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            // Nếu quay lại từ EditStoryActivity, reload lại danh sách
            reloadStories()
        }
    }
}
