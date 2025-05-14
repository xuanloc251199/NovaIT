package com.hctien.novait.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.adapter.ChapterAdminAdapter
import com.hctien.novait.data.DBHelper
import com.hctien.novait.model.Chapter
import com.hctien.novait.Story
import com.hctien.novait.activity.admin.EditChapterActivity

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addChapterButton: Button
    private lateinit var dbHelper: DBHelper
    private lateinit var chapterAdminAdapter: ChapterAdminAdapter
    private lateinit var titleTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var chapterCountTextView: TextView
    private var storyId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)

        // Initializing views
        recyclerView = findViewById(R.id.recyclerViewChapters)
        addChapterButton = findViewById(R.id.addChapterButton)
        titleTextView = findViewById(R.id.titleTextView)
        authorTextView = findViewById(R.id.authorTextView)
        contentTextView = findViewById(R.id.contentTextView)
        chapterCountTextView = findViewById(R.id.chapterCountTextView)

        dbHelper = DBHelper(this)

        // Getting storyId from Intent
        storyId = intent.getIntExtra("storyId", -1)

        if (storyId != -1) {
            // Fetching story details from DB
            val story = dbHelper.getStoryById(storyId)
            titleTextView.text = story.title
            authorTextView.text = "Tác giả: ${story.author}"
            contentTextView.text = story.content

            // Fetching chapter count from DB
            val chapterCount = dbHelper.getChapterCountByStoryId(story.id)
            chapterCountTextView.text = "Chương: $chapterCount"

            // Fetching chapters for this story
            val chapters = dbHelper.getChaptersByStoryId(story.id)

            // Setting up RecyclerView with ChapterAdminAdapter
            chapterAdminAdapter = ChapterAdminAdapter(
                this,
                chapters,
                { chapter ->
                    // Handle edit chapter click
                    val intent = Intent(this, EditChapterActivity::class.java)
                    intent.putExtra("chapterId", chapter.id)
                    startActivityForResult(intent, REQUEST_CODE_EDIT_CHAPTER)
                },
                { chapter ->
                    // Handle delete chapter click
                    val result = dbHelper.deleteChapter(chapter.id)
                    if (result > 0) {
                        Toast.makeText(this, "Chương đã bị xóa", Toast.LENGTH_SHORT).show()
                        reloadChapters()
                    } else {
                        Toast.makeText(this, "Không thể xóa chương", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = chapterAdminAdapter
        } else {
            Toast.makeText(this, "Không tìm thấy truyện", Toast.LENGTH_SHORT).show()
        }

        // Handling add chapter button click
        addChapterButton.setOnClickListener {
            val intent = Intent(this, EditChapterActivity::class.java)
            intent.putExtra("storyId", storyId) // Passing storyId to EditChapterActivity
            startActivityForResult(intent, REQUEST_CODE_ADD_CHAPTER)
        }
    }

    // Reload chapters after editing or deleting
    private fun reloadChapters() {
        val chapters = dbHelper.getChaptersByStoryId(storyId)
        chapterAdminAdapter = ChapterAdminAdapter(
            this,
            chapters,
            { chapter ->
                val intent = Intent(this, EditChapterActivity::class.java)
                intent.putExtra("chapterId", chapter.id)
                startActivityForResult(intent, REQUEST_CODE_EDIT_CHAPTER)
            },
            { chapter ->
                val result = dbHelper.deleteChapter(chapter.id)
                if (result > 0) {
                    Toast.makeText(this, "Chương đã bị xóa", Toast.LENGTH_SHORT).show()
                    reloadChapters()
                } else {
                    Toast.makeText(this, "Không thể xóa chương", Toast.LENGTH_SHORT).show()
                }
            }
        )
        recyclerView.adapter = chapterAdminAdapter
    }

    companion object {
        const val REQUEST_CODE_ADD_CHAPTER = 1001
        const val REQUEST_CODE_EDIT_CHAPTER = 2001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            // Reload chapters after editing or adding
            reloadChapters()
        }
    }
}
