package com.hctien.novait.activity.client

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.R
import com.hctien.novait.data.DBHelper

class EditNameActivity : AppCompatActivity() {

    private lateinit var displayNameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DBHelper
    private var userId: Int = 0  // Lấy userId từ SharedPreferences hoặc Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_name)

        displayNameEditText = findViewById(R.id.displayNameEditText)
        saveButton = findViewById(R.id.saveButton)

        dbHelper = DBHelper(this)

        // Lấy userId từ SharedPreferences hoặc Intent
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("userId", 0) // Hoặc lấy từ Intent nếu cần

        // Lấy tên hiển thị hiện tại và hiển thị lên EditText
        val currentDisplayName = dbHelper.getUserDisplayName(userId)  // Hàm lấy tên hiển thị từ DB
        displayNameEditText.setText(currentDisplayName)

        saveButton.setOnClickListener {
            val newDisplayName = displayNameEditText.text.toString()

            if (newDisplayName.isNotEmpty()) {
                val result = dbHelper.updateDisplayName(userId, newDisplayName)

                if (result > 0) {
                    Toast.makeText(this, "Tên hiển thị đã được cập nhật", Toast.LENGTH_SHORT).show()

                    // Cập nhật lại tên hiển thị trong SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("displayName", newDisplayName)  // Save the new display name
                    editor.apply()
                    finish()
                } else {
                    Toast.makeText(this, "Không thể cập nhật tên hiển thị", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tên hiển thị không được để trống", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
