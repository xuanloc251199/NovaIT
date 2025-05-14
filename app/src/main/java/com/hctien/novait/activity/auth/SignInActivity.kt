package com.hctien.novait.activity.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.data.DBHelper
import com.hctien.novait.MainActivity
import com.hctien.novait.R

class SignInActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signInButton = findViewById(R.id.signInButton)
        signUpButton = findViewById(R.id.signUpButton)

        // Lấy SharedPreferences để lưu trạng thái đăng nhập và vai trò
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Đăng nhập
        signInButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val dbHelper = DBHelper(this)

                // Kiểm tra thông tin người dùng và lấy User
                val user = dbHelper.checkUser(username, password)

                if (user != null) {
                    // Lưu trạng thái đăng nhập và thông tin người dùng vào SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("userRole", user.role)  // Lưu vai trò người dùng
                    editor.putString("username", user.username)  // Lưu tên người dùng
                    editor.putString("displayName", user.username)  // Lưu tên người dùng
                    editor.putInt("userId", user.id)  // Lưu ID người dùng
                    editor.apply()

                    // Nếu đăng nhập thành công, chuyển sang MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Thông báo lỗi nếu đăng nhập không thành công
                    Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // Chuyển đến màn hình đăng ký nếu người dùng chưa có tài khoản
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
