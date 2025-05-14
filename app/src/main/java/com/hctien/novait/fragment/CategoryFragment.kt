package com.hctien.novait.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.adapter.CategoryAdapter
import com.hctien.novait.data.DBHelper
import com.hctien.novait.Category
import com.hctien.novait.activity.client.StoriesByCategoryActivity  // Import Activity hiển thị truyện theo thể loại

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        // Khởi tạo RecyclerView
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory)

        // Gán LinearLayoutManager cho RecyclerView
        recyclerViewCategory.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo DBHelper
        dbHelper = DBHelper(requireContext())

        // Lấy tất cả thể loại từ cơ sở dữ liệu
        val categories = dbHelper.getCategories()

        // Cập nhật RecyclerView với dữ liệu thể loại
        categoryAdapter = CategoryAdapter(categories) { category ->
            // Khi người dùng nhấn vào một thể loại, chuyển đến màn hình hiển thị truyện theo thể loại đó
            val intent = Intent(requireContext(), StoriesByCategoryActivity::class.java)
            intent.putExtra("categoryId", category.categoryId)  // Truyền categoryId
            startActivity(intent)
        }
        recyclerViewCategory.adapter = categoryAdapter

        return view
    }
}
