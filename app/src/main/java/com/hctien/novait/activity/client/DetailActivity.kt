package com.hctien.novait.activity.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hctien.novait.ChapterAdapter
import com.hctien.novait.R
import com.hctien.novait.Story
import com.hctien.novait.adapter.CommentAdapter
import com.hctien.novait.data.DBHelper
import com.hctien.novait.model.Comment

class DetailActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var chapterCountTextView: TextView
    private lateinit var viewCountTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var coverImageView: ImageView
    private lateinit var recyclerViewChapters: RecyclerView
    private lateinit var chapterAdapter: ChapterAdapter
    private lateinit var story: Story
    private lateinit var dbHelper: DBHelper

    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var editTextComment: EditText
    private lateinit var buttonSendComment: Button
    private var comments: MutableList<Comment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Lấy các View từ layout
        titleTextView = findViewById(R.id.detailTitle)
        authorTextView = findViewById(R.id.detailAuthor)
        chapterCountTextView = findViewById(R.id.detailChapterCount)
        viewCountTextView = findViewById(R.id.detailViewCount)
        ratingTextView = findViewById(R.id.detailRating)
        statusTextView = findViewById(R.id.detailStatus)
        contentTextView = findViewById(R.id.detailContent)
        coverImageView = findViewById(R.id.detailCoverImage)
        recyclerViewChapters = findViewById(R.id.recyclerViewChapters)
        recyclerViewComments = findViewById(R.id.recyclerViewComments)
        editTextComment = findViewById(R.id.editTextComment)
        buttonSendComment = findViewById(R.id.buttonSendComment)

        // Khởi tạo DBHelper
        dbHelper = DBHelper(this)

        // Lấy story_id từ Intent
        val storyId = intent.getIntExtra("story_id", -1)

        // Lấy đối tượng Story từ cơ sở dữ liệu bằng storyId
        story = dbHelper.getStoryById(storyId)

        // Hiển thị thông tin truyện
        titleTextView.text = story.title
        authorTextView.text = "Tác giả: ${story.author}"
        chapterCountTextView.text = "Số chương: ${story.chapterCount}"
        viewCountTextView.text = "Lượt xem: ${story.viewCount}"
        ratingTextView.text = "Đánh giá: ${story.rating}"
        statusTextView.text = "Trạng thái: ${story.status}"
        contentTextView.text = story.content

        // Hiển thị ảnh bìa truyện
        Glide.with(this)
            .load(story.coverImageUrl)
            .into(coverImageView)

        // Lấy danh sách các chương của truyện
        val chapters = dbHelper.getChaptersByStoryId(storyId)

        // Thiết lập RecyclerView với ChapterAdapter
        chapterAdapter = ChapterAdapter(chapters) { chapter ->
            // Khi người dùng nhấn vào một chương, chuyển đến ChapterActivity
            val intent = Intent(this, ChapterActivity::class.java)
            intent.putExtra("chapter", chapter)  // Truyền đối tượng Chapter vào Intent
            startActivity(intent)
        }

        recyclerViewChapters.layoutManager = LinearLayoutManager(this)
        recyclerViewChapters.adapter = chapterAdapter

        // Lấy danh sách comment
        comments = dbHelper.getCommentsByStoryId(storyId).toMutableList()
        commentAdapter = CommentAdapter(comments)
        recyclerViewComments.layoutManager = LinearLayoutManager(this)
        recyclerViewComments.adapter = commentAdapter


        // Gửi bình luận mới
        buttonSendComment.setOnClickListener {
            val text = editTextComment.text.toString().trim()
            if (text.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("userId", 0)
                dbHelper.addComment(storyId, userId, text)

                // Xóa hết và thêm lại danh sách mới
                comments.clear()
                comments.addAll(dbHelper.getCommentsByStoryId(storyId))
                commentAdapter.notifyDataSetChanged()

                editTextComment.text.clear()
                recyclerViewComments.scrollToPosition(0)
            }
        }
    }
}
