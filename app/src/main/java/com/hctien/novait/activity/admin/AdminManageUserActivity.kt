package com.hctien.novait.activity.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.User
import com.hctien.novait.adapter.AdminUserAdapter
import com.hctien.novait.data.DBHelper

class AdminManageUserActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminUserAdapter
    private lateinit var dbHelper: DBHelper
    private var users: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_user)

        dbHelper = DBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewUsers)
        findViewById<Button>(R.id.btnAddUser).setOnClickListener { showAddUserDialog() }

        users = dbHelper.getAllClients()
        adapter = AdminUserAdapter(users,
            onEdit = { user -> showEditUserDialog(user) },
            onDelete = { user -> showDeleteUserDialog(user) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadUserList() {
        users.clear()
        users.addAll(dbHelper.getAllClients())
        adapter.notifyDataSetChanged()
    }

    private fun showEditUserDialog(user: User) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_user, null)
        val editDisplayName = dialogView.findViewById<EditText>(R.id.editDisplayName)
        val editPassword = dialogView.findViewById<EditText>(R.id.editPassword)

        editDisplayName.setText(user.displayName)

        AlertDialog.Builder(this)
            .setTitle("Chỉnh sửa tài khoản")
            .setView(dialogView)
            .setPositiveButton("Lưu") { dialog, _ ->
                val newDisplayName = editDisplayName.text.toString().trim()
                val newPassword = editPassword.text.toString().trim()
                if (newDisplayName.isNotBlank()) dbHelper.updateDisplayName(user.id, newDisplayName)
                if (newPassword.isNotBlank()) dbHelper.updatePassword(user.username, newPassword)
                loadUserList()
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDeleteUserDialog(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Xóa tài khoản")
            .setMessage("Bạn có chắc chắn muốn xóa tài khoản này?")
            .setPositiveButton("Xóa") { dialog, _ ->
                dbHelper.deleteUser(user.id)
                loadUserList()
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showAddUserDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_user, null)
        val editUsername = dialogView.findViewById<EditText>(R.id.editUsername)
        val editDisplayName = dialogView.findViewById<EditText>(R.id.editDisplayName)
        val editPassword = dialogView.findViewById<EditText>(R.id.editPassword)

        AlertDialog.Builder(this)
            .setTitle("Thêm tài khoản mới")
            .setView(dialogView)
            .setPositiveButton("Thêm") { dialog, _ ->
                val username = editUsername.text.toString().trim()
                val displayName = editDisplayName.text.toString().trim()
                val password = editPassword.text.toString().trim()
                if (username.isNotBlank() && displayName.isNotBlank() && password.isNotBlank()) {
                    dbHelper.addUser(username, password, displayName)
                    loadUserList()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
