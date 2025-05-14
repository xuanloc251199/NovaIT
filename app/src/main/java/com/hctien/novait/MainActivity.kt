package com.hctien.novait

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hctien.novait.fragment.HomeFragment
import com.hctien.novait.fragment.CategoryFragment
import com.hctien.novait.fragment.ProfileFragment
import com.hctien.novait.fragment.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo BottomNavigationView
        val bottomNav: BottomNavigationView = findViewById(R.id.nav_view)

        // Set up BottomNavigationView với NavController
        bottomNav.setOnNavigationItemSelectedListener {
            val selectedFragment: Fragment = when (it.itemId) {
                R.id.nav_home -> HomeFragment()  // Chuyển sang HomeFragment
                R.id.nav_search -> SearchFragment()  // Chuyển sang SearchFragment
                R.id.nav_category -> CategoryFragment()  // Chuyển sang LibraryFragment
                R.id.nav_profile -> ProfileFragment()  // Chuyển sang ProfileFragment
                else -> HomeFragment()  // Mặc định là HomeFragment
            }
            // Chuyển Fragment khi người dùng chọn item trên Bottom Navigation
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, selectedFragment)
                .commit()
            true
        }

        // Mặc định khi vừa vào MainActivity sẽ hiển thị HomeFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment())
                .commit()
        }
    }
}
