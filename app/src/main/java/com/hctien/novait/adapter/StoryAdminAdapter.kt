package com.hctien.novait.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.Story
import com.hctien.novait.Category
import com.bumptech.glide.Glide
import com.hctien.novait.data.DBHelper

class StoryAdminAdapter(
    private val context: Context,
    private var stories: List<Story>,
    private val categories: List<Category>,
    private val userRole: String,
    val dbHelper: DBHelper,
    private val onEditClick: (Story) -> Unit,
    private val onDeleteClick: (Story) -> Unit,
    private val onStoryClick: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdminAdapter.StoryViewHolder>() {

    // Tạo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_story_admin, parent, false)
        return StoryViewHolder(view)
    }

    // Liên kết dữ liệu vào các view
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]

        // Hiển thị tên truyện, tác giả, thể loại, trạng thái và thông tin khác
        holder.titleTextView.text = story.title
        holder.authorTextView.text = "Tác giả: ${story.author}"
        holder.statusTextView.text = "Trạng thái: ${story.status}"
        holder.viewCountTextView.text = "Lượt xem: ${story.viewCount}"
        holder.ratingTextView.text = "Đánh giá: ${story.rating}"

        // Lấy số chương thực tế từ cơ sở dữ liệu
        val chapterCount = dbHelper.getChapterCountByStoryId(story.id)
        holder.chapterCountTextView.text = "Chương: $chapterCount"

        // Hiển thị ảnh bìa
        Glide.with(context)
            .load(story.coverImageUrl)
            .into(holder.coverImageView)

        // Hiển thị thông tin thể loại
        val categoryName = categories.find { it.categoryId == story.categoryId }?.categoryName
        holder.categoryTextView.text = categoryName ?: "Chưa phân loại"

        // Hiển thị nút Sửa và Xóa nếu người dùng là admin
        if (userRole == "admin") {
            holder.editButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE
        } else {
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }

        // Thiết lập sự kiện cho nút Sửa và Xóa
        holder.editButton.setOnClickListener {
            onEditClick(story)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(story)
        }

        holder.itemView.setOnClickListener {
            onStoryClick(story)
        }
    }

    // Trả về số lượng truyện
    override fun getItemCount(): Int {
        return stories.size
    }

    // Cập nhật dữ liệu truyện trong adapter sau khi thêm, sửa, xóa
    fun updateStories(updatedStories: List<Story>) {
        stories = updatedStories
        notifyDataSetChanged()
    }

    // ViewHolder để hiển thị các item trong RecyclerView
    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.coverImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val chapterCountTextView: TextView = itemView.findViewById(R.id.chapterCountTextView)
        val viewCountTextView: TextView = itemView.findViewById(R.id.viewCountTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val editButton: CardView = itemView.findViewById(R.id.editButton)
        val deleteButton: CardView = itemView.findViewById(R.id.deleteButton)
    }
}
