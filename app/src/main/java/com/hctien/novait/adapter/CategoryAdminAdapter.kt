package com.hctien.novait.adapter.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.Category

class CategoryAdminAdapter(
    private val context: Context,
    private var categories: List<Category>,
    private val onEditClick: (Category) -> Unit,
    private val onDeleteClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdminAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category_admin, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.categoryNameTextView.text = category.categoryName

        // Hiển thị nút Sửa và Xóa
        holder.editButton.setOnClickListener {
            onEditClick(category)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    // Cập nhật lại danh sách thể loại
    fun updateCategories(updatedCategories: List<Category>) {
        categories = updatedCategories
        notifyDataSetChanged()
    }
}
