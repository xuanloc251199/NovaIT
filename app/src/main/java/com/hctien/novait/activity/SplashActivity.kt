package com.hctien.novait.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.hctien.novait.MainActivity
import com.hctien.novait.R
import com.hctien.novait.activity.auth.SignInActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Kiểm tra trạng thái đăng nhập và vai trò người dùng
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            // Nếu người dùng đã đăng nhập, chuyển đến MainActivity, nếu chưa, chuyển đến SignInActivity
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
            finish()
        }, 2000)  // Splash time
    }
}
