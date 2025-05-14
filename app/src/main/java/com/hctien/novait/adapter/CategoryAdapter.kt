package com.hctien.novait.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.hctien.novait.Category
import com.hctien.novait.R

class CategoryAdapter(
    private val categories: List<Category>,
    private val onClick: (Category) -> Unit  // Sự kiện click khi người dùng chọn thể loại
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // Tạo ViewHolder cho mỗi item trong RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Gắn dữ liệu vào mỗi ViewHolder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryNameTextView.text = category.categoryName

        // Thêm sự kiện click vào item
        holder.itemView.setOnClickListener {
            onClick(category)
        }
    }

    // Trả về số lượng item trong RecyclerView
    override fun getItemCount(): Int = categories.size

    // ViewHolder cho mỗi item trong RecyclerView
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryName)
    }
}
