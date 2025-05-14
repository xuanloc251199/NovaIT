package com.hctien.novait.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.model.CommentDetail

class AdminCommentAdapter(
    private var comments: MutableList<CommentDetail>,
    private val onDelete: (CommentDetail) -> Unit
) : RecyclerView.Adapter<AdminCommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvStoryTitle: TextView = itemView.findViewById(R.id.tvStoryTitle)
        val tvDisplayName: TextView = itemView.findViewById(R.id.tvDisplayName)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment_admin, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.tvStoryTitle.text = "Truyện: ${comment.storyTitle}"
        holder.tvDisplayName.text = "Người bình luận: ${comment.displayName}"
        holder.tvContent.text = "Nội dung: ${comment.text}"
        holder.tvTime.text = "Thời gian: ${android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", comment.timestamp)}"
        holder.btnDelete.setOnClickListener { onDelete(comment) }
    }

    fun updateData(newComments: List<CommentDetail>) {
        comments.clear()
        comments.addAll(newComments)
        notifyDataSetChanged()
    }
}
