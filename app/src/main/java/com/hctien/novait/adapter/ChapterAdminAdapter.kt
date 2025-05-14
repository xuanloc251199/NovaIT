package com.hctien.novait.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.model.Chapter

class ChapterAdminAdapter(
    private val context: Context,
    private val chapters: List<Chapter>,
    private val onEditClick: (Chapter) -> Unit,
    private val onDeleteClick: (Chapter) -> Unit
) : RecyclerView.Adapter<ChapterAdminAdapter.ChapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chapter_admin, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.bind(chapter)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    inner class ChapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val chapterTitleTextView: TextView = view.findViewById(R.id.chapterTitleTextView)
        private val chapterContentTextView: TextView = view.findViewById(R.id.chapterContentTextView)
        private val editButton: CardView = view.findViewById(R.id.editChapterButton)
        private val deleteButton: CardView = view.findViewById(R.id.deleteChapterButton)

        fun bind(chapter: Chapter) {
            chapterTitleTextView.text = chapter.title
            chapterContentTextView.text = chapter.content

            // Xử lý sự kiện khi nhấn nút sửa
            editButton.setOnClickListener {
                onEditClick(chapter)
            }

            // Xử lý sự kiện khi nhấn nút xóa
            deleteButton.setOnClickListener {
                onDeleteClick(chapter)
            }
        }
    }
}
