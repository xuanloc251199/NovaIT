package com.hctien.novait.activity.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.R
import com.hctien.novait.data.DBHelper

class EditCategoryActivity : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var saveCategoryButton: Button
    private lateinit var dbHelper: DBHelper
    private var categoryId: Int = -1  // ID của thể loại, sẽ được truyền qua nếu là sửa thể loại

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        categoryNameEditText = findViewById(R.id.categoryNameEditText)
        saveCategoryButton = findViewById(R.id.saveCategoryButton)
        dbHelper = DBHelper(this)

        // Lấy ID thể loại từ Intent (nếu có)
        categoryId = intent.getIntExtra("categoryId", -1)

        if (categoryId != -1) {
            // Nếu là sửa thể loại, lấy thông tin thể loại cũ
            val category = dbHelper.getCategoryById(categoryId)
            category?.let {
                categoryNameEditText.setText(it.categoryName)
            }
        }

        saveCategoryButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString()

            if (categoryName.isNotEmpty()) {
                if (categoryId == -1) {
                    // Thêm thể loại mới
                    val result = dbHelper.addCategory(categoryName)
                    if (result != -1L) {
                        Toast.makeText(this, "Thể loại đã được thêm", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Không thể thêm thể loại", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Cập nhật thể loại đã có
                    val result = dbHelper.updateCategory(categoryId, categoryName)
                    if (result > 0) {
                        Toast.makeText(this, "Thể loại đã được cập nhật", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Không thể cập nhật thể loại", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
