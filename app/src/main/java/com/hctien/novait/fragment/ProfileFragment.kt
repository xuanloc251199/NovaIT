package com.hctien.novait.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hctien.novait.R
import com.hctien.novait.activity.admin.AdminManageCommentActivity
import com.hctien.novait.activity.client.EditNameActivity
import com.hctien.novait.activity.admin.AdminManageUserActivity
import com.hctien.novait.activity.admin.ManageCategoriesActivity
import com.hctien.novait.activity.client.ChangePasswordActivity
import com.hctien.novait.activity.admin.ManageStoriesActivity
import com.hctien.novait.activity.admin.StatisticsActivity
import com.hctien.novait.activity.auth.SignInActivity

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var welcomeTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var libraryManagementLayout: LinearLayout
    private lateinit var changeNameButton: Button
    private lateinit var changePasswordButton: Button
    private lateinit var manageBooksButton: Button
    private lateinit var signOutButton: Button
    private lateinit var manageUsersButton: Button
    private lateinit var manageCategoriesButton: Button
    private lateinit var manageCommentButton: Button
    private lateinit var manageStatisticsButton: Button

    private lateinit var sharedPreferences: SharedPreferences
    private var userRole: String? = null
    private var username: String? = null
    private var displayName: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        libraryManagementLayout = view.findViewById(R.id.libraryManagementLayout)
        changePasswordButton = view.findViewById(R.id.changePasswordButton)
        changeNameButton = view.findViewById(R.id.changeUsernameButton)
        signOutButton = view.findViewById(R.id.signOutButton)
        manageBooksButton = view.findViewById(R.id.manageBooksButton)
        manageUsersButton = view.findViewById(R.id.manageUsersButton)
        manageCategoriesButton = view.findViewById(R.id.manageCategoriesButton)
        manageCommentButton = view.findViewById(R.id.manageCommentButton)
        manageStatisticsButton = view.findViewById(R.id.manageStatisticsButton)

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        userRole = sharedPreferences.getString("userRole", "client")
        username = sharedPreferences.getString("username", "Unknown User")
        displayName = sharedPreferences.getString("displayName", "No Name")

        usernameTextView.text = "Welcome, $displayName!"

        if (userRole == "admin") {
            libraryManagementLayout.visibility = View.VISIBLE
        } else {
            libraryManagementLayout.visibility = View.GONE
        }

        changeNameButton.setOnClickListener {
            // Khi nhấn nút đổi tên, chuyển đến EditNameActivity
            val intent = Intent(requireContext(), EditNameActivity::class.java)
            startActivity(intent)
        }

        changePasswordButton.setOnClickListener {
            // Chuyển đến màn hình đổi mật khẩu
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        manageBooksButton.setOnClickListener {
            // Chuyển đến màn hình đổi mật khẩu
            val intent = Intent(requireContext(), ManageStoriesActivity::class.java)
            startActivity(intent)
        }

        manageUsersButton.setOnClickListener {
            // Chuyển đến màn hình đổi mật khẩu
            val intent = Intent(requireContext(), AdminManageUserActivity::class.java)
            startActivity(intent)
        }

        manageCategoriesButton.setOnClickListener {
            // Chuyển đến màn hình quản lý thể loại
            val intent = Intent(requireContext(), ManageCategoriesActivity::class.java)
            startActivity(intent)
        }

        manageCommentButton.setOnClickListener {
            val intent = Intent(requireContext(), AdminManageCommentActivity::class.java)
            startActivity(intent)
        }

        manageStatisticsButton.setOnClickListener {
            val intent = Intent(requireContext(), StatisticsActivity::class.java)
            startActivity(intent)
        }

        signOutButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.remove("userRole")
            editor.remove("username")
            editor.remove("userId")
            editor.remove("displayName") // Remove display name on sign out
            editor.apply()

            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Lấy lại tên hiển thị mới nhất từ SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        displayName = sharedPreferences.getString("displayName", "No Name")
        usernameTextView.text = "Welcome, $displayName!"
    }
}
