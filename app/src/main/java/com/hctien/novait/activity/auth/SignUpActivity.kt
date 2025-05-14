package com.hctien.novait.activity.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.data.DBHelper
import com.hctien.novait.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var displayNameEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        displayNameEditText = findViewById(R.id.displayNameEditText)
        signUpButton = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val displayName = displayNameEditText.text.toString()

            // Kiểm tra nếu các trường nhập không rỗng
            if (username.isNotEmpty() && password.isNotEmpty() && displayName.isNotEmpty()) {
                val dbHelper = DBHelper(this)
                val result = dbHelper.addUser(username, password, displayName)

                if (result == -1L) {
                    Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                    finish()  // Đóng màn hình đăng ký và trở lại màn hình đăng nhập
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
