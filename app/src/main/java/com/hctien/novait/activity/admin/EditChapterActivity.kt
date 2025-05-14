package com.hctien.novait.activity.admin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.R
import com.hctien.novait.data.DBHelper
import com.hctien.novait.model.Chapter

class EditChapterActivity : AppCompatActivity() {

    private lateinit var chapterTitleEditText: EditText
    private lateinit var chapterContentEditText: EditText
    private lateinit var saveChapterButton: Button
    private lateinit var cancelButton: Button
    private lateinit var dbHelper: DBHelper
    private var storyId: Int = -1
    private var chapterId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_chapter)

        // Khởi tạo các view
        chapterTitleEditText = findViewById(R.id.chapterTitleEditText)
        chapterContentEditText = findViewById(R.id.chapterContentEditText)
        saveChapterButton = findViewById(R.id.saveChapterButton)
        cancelButton = findViewById(R.id.cancelButton)

        dbHelper = DBHelper(this)

        // Lấy storyId từ Intent (cần phải có trước khi chỉnh sửa chapter)
        storyId = intent.getIntExtra("storyId", -1)
        chapterId = intent.getIntExtra("chapterId", -1)

        if (chapterId != -1) {
            // Nếu là chỉnh sửa chapter, lấy thông tin của chapter từ DB và hiển thị lên
            val chapter = dbHelper.getChapterById(chapterId)
            chapterTitleEditText.setText(chapter.title)
            chapterContentEditText.setText(chapter.content)
        }

        // Lưu thông tin chapter
        saveChapterButton.setOnClickListener {
            val chapterTitle = chapterTitleEditText.text.toString()
            val chapterContent = chapterContentEditText.text.toString()

            if (chapterTitle.isNotEmpty() && chapterContent.isNotEmpty()) {
                if (chapterId == -1) {
                    // Nếu không có chapterId, tức là đang thêm chapter mới
                    val newChapter = Chapter(
                        storyId = storyId,
                        title = chapterTitle,
                        content = chapterContent
                    )
                    dbHelper.addChapter(newChapter)
                } else {
                    // Nếu có chapterId, tức là đang chỉnh sửa chapter
                    val updatedChapter = Chapter(
                        id = chapterId,
                        storyId = storyId,
                        title = chapterTitle,
                        content = chapterContent
                    )
                    dbHelper.updateChapter(updatedChapter)
                }

                // Thông báo thành công
                Toast.makeText(this, "Chapter đã được lưu", Toast.LENGTH_SHORT).show()
                finish()  // Quay lại màn hình chi tiết truyện
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // Hủy chỉnh sửa và quay lại
        cancelButton.setOnClickListener {
            finish()
        }
    }
}
