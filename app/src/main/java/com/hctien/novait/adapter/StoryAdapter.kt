package com.hctien.novait.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hctien.novait.Category
import com.hctien.novait.R
import com.hctien.novait.Story
import com.hctien.novait.data.DBHelper
import com.hctien.novait.data.FormatHelper

class StoryAdapter(
    private val stories: List<Story>,
    private val categories: List<Category>,
    private val dbHelper: DBHelper,
    private val onStoryClick: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        val formattedViewCount = FormatHelper().formatViewCount(story.viewCount)

        holder.titleTextView.text = story.title
        holder.authorTextView.text = "Tác giả: ${story.author}"
        holder.viewCountTextView.text = "Lượt xem: $formattedViewCount"
        holder.ratingTextView.text = "Đánh giá: ${story.rating}"
        val chapterCount = dbHelper.getChapterCountByStoryId(story.id)
        holder.chapterCountTextView.text = "Chương: $chapterCount"
        val categoryName = categories.find { it.categoryId == story.categoryId }?.categoryName ?: "Không xác định"
        holder.categoryTextView.text = "Thể loại: $categoryName"
        holder.statusTextView.text = "Trạng thái: ${story.status}"

        Glide.with(holder.coverImageView.context)
            .load(story.coverImageUrl)
            .into(holder.coverImageView)

        holder.itemView.setOnClickListener {
            // Tăng lượt xem trong DB
            dbHelper.increaseStoryViewCount(story.id)
            story.viewCount += 1
            notifyItemChanged(position)
            // Gọi callback cho Fragment để chuyển trang
            onStoryClick(story)
        }
    }

    override fun getItemCount(): Int = stories.size

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.storyTitle)
        val authorTextView: TextView = itemView.findViewById(R.id.storyAuthor)
        val chapterCountTextView: TextView = itemView.findViewById(R.id.storyChapterCount)
        val viewCountTextView: TextView = itemView.findViewById(R.id.storyViewCount)
        val ratingTextView: TextView = itemView.findViewById(R.id.storyRating)
        val categoryTextView: TextView = itemView.findViewById(R.id.storyCategory)
        val statusTextView: TextView = itemView.findViewById(R.id.storyStatus)
        val coverImageView: ImageView = itemView.findViewById(R.id.storyCoverImage)
    }
}
