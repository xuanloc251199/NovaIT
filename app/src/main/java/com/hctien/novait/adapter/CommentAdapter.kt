package com.hctien.novait.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.model.Comment
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(private val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.displayNameTextView.text = comment.displayName
        holder.commentTextView.text = comment.text
        holder.timestampTextView.text = formatTimestamp(comment.timestamp)
    }

    override fun getItemCount() = comments.size

    private fun formatTimestamp(ts: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(ts))
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val displayNameTextView: TextView = itemView.findViewById(R.id.commentDisplayName)
        val commentTextView: TextView = itemView.findViewById(R.id.commentText)
        val timestampTextView: TextView = itemView.findViewById(R.id.commentTimestamp)
    }
}
