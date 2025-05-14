package com.hctien.novait.activity.client

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.R
import com.hctien.novait.data.DBHelper

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var changePasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        changePasswordButton = findViewById(R.id.changePasswordButton)

        val dbHelper = DBHelper(this)

        changePasswordButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    val username = sharedPreferences.getString("username", null)

                    if (username != null && dbHelper.checkUser(username, currentPassword) != null) {
                        val result = dbHelper.updatePassword(username, newPassword)

                        if (result > 0) {
                            Toast.makeText(this, "Mật khẩu đã được cập nhật", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Lỗi khi cập nhật mật khẩu", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
