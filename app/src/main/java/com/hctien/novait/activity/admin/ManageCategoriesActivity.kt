package com.hctien.novait.activity.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.adapter.admin.CategoryAdminAdapter
import com.hctien.novait.data.DBHelper

class ManageCategoriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addCategoryButton: Button
    private lateinit var dbHelper: DBHelper
    private lateinit var categoryAdminAdapter: CategoryAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_categories)

        recyclerView = findViewById(R.id.recyclerViewCategories)
        addCategoryButton = findViewById(R.id.addCategoryButton)
        dbHelper = DBHelper(this)

        // Cập nhật RecyclerView với danh sách thể loại
        addCategoryButton.setOnClickListener {
            val intent = Intent(this, EditCategoryActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
        }

        // Khi người dùng nhấn vào thể loại để sửa
        categoryAdminAdapter = CategoryAdminAdapter(this, dbHelper.getCategories(),
            { category ->
                val intent = Intent(this, EditCategoryActivity::class.java)
                intent.putExtra("categoryId", category.categoryId)
                startActivityForResult(intent, REQUEST_CODE_EDIT_CATEGORY)
            },
            { category ->
                val result = dbHelper.deleteCategory(category.categoryId)
                if (result > 0) {
                    Toast.makeText(this, "Thể loại đã bị xóa", Toast.LENGTH_SHORT).show()
                    reloadCategories()
                } else {
                    Toast.makeText(this, "Không thể xóa thể loại", Toast.LENGTH_SHORT).show()
                }
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = categoryAdminAdapter
    }

    // Hàm để reload lại danh sách thể loại
    private fun reloadCategories() {
        val categories = dbHelper.getCategories()
        categoryAdminAdapter.updateCategories(categories)
    }

    // Nhận kết quả từ EditCategoryActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // Reload lại danh sách thể loại sau khi sửa hoặc thêm thể loại
            reloadCategories()
        }
    }

    companion object {
        const val REQUEST_CODE_EDIT_CATEGORY = 1001
        const val REQUEST_CODE_ADD_CATEGORY = 1002
    }
}
