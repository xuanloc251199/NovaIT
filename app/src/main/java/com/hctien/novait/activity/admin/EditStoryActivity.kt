package com.hctien.novait.activity.admin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.R
import com.hctien.novait.data.DBHelper
import com.hctien.novait.Story
import com.hctien.novait.Category

class EditStoryActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var coverImageUrlEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DBHelper
    private var storyId: Int = -1
    private var categories: List<Category> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_story)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        authorEditText = findViewById(R.id.authorEditText)
        categorySpinner = findViewById(R.id.categorySpinner)
        coverImageUrlEditText = findViewById(R.id.coverImageUrlEditText)
        saveButton = findViewById(R.id.saveButton)

        dbHelper = DBHelper(this)

        // Lấy danh sách thể loại từ DB
        categories = dbHelper.getCategories()

        // Tạo một ArrayAdapter cho Spinner
        val categoryNames = categories.map { it.categoryName }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Lấy ID truyện từ Intent (nếu có) để chỉnh sửa
        storyId = intent.getIntExtra("storyId", -1)

        if (storyId != -1) {
            // Nếu là sửa truyện, lấy thông tin truyện cũ
            val story = dbHelper.getStoryById(storyId)
            titleEditText.setText(story.title)
            contentEditText.setText(story.content)
            authorEditText.setText(story.author)

            // Chọn thể loại đã có cho truyện này
            val categoryIndex = categories.indexOfFirst { it.categoryId == story.categoryId }
            if (categoryIndex != -1) {
                categorySpinner.setSelection(categoryIndex)
            }

            coverImageUrlEditText.setText(story.coverImageUrl)  // Hiển thị URL ảnh
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val author = authorEditText.text.toString()
            val selectedCategory = categorySpinner.selectedItemPosition
            val categoryId = categories[selectedCategory].categoryId  // Lấy ID thể loại từ Spinner
            val coverImageUrl = coverImageUrlEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty() && author.isNotEmpty() && coverImageUrl.isNotEmpty()) {
                if (storyId == -1) {
                    // Thêm truyện mới
                    val story = Story(
                        title = title,
                        content = content,
                        author = author,
                        chapterCount = 0,
                        viewCount = 0,
                        rating = 0f,
                        categoryId = categoryId,
                        status = "Mới đăng",
                        coverImageUrl = coverImageUrl  // Lưu ảnh từ URL
                    )
                    dbHelper.addStory(story)
                } else {
                    // Cập nhật truyện đã tồn tại
                    val story = Story(
                        id = storyId,
                        title = title,
                        content = content,
                        author = author,
                        chapterCount = 0,
                        viewCount = 0,
                        rating = 0f,
                        categoryId = categoryId,
                        status = "Mới cập nhật",
                        coverImageUrl = coverImageUrl  // Lưu ảnh từ URL
                    )
                    dbHelper.updateStory(story)
                }

                Toast.makeText(this, "Truyện đã được lưu", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()  // Quay lại màn hình quản lý truyện
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
