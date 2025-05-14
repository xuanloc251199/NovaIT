package com.hctien.novait.activity.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.adapter.AdminCommentAdapter
import com.hctien.novait.data.DBHelper
import com.hctien.novait.model.CommentDetail

class AdminManageCommentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminCommentAdapter
    private lateinit var dbHelper: DBHelper
    private var comments: MutableList<CommentDetail> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_comment)

        dbHelper = DBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewComments)

        comments = dbHelper.getAllCommentsDetail().toMutableList()
        adapter = AdminCommentAdapter(comments) { comment ->
            AlertDialog.Builder(this)
                .setTitle("Xóa bình luận")
                .setMessage("Bạn có chắc chắn muốn xóa bình luận này?")
                .setPositiveButton("Xóa") { dialog, _ ->
                    dbHelper.deleteComment(comment.id)
                    loadCommentList()
                    dialog.dismiss()
                }
                .setNegativeButton("Hủy", null)
                .show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadCommentList() {
        comments.clear()
        comments.addAll(dbHelper.getAllCommentsDetail())
        adapter.notifyDataSetChanged()
    }
}
