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
import com.hctien.novait.activity.client.DetailActivity
import com.hctien.novait.adapter.StoryAdapter
import com.hctien.novait.data.DBHelper
import com.hctien.novait.data.FormatHelper

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var recyclerViewNew: RecyclerView
    private lateinit var recyclerViewUpdated: RecyclerView
    private lateinit var recyclerViewCompleted: RecyclerView
    private lateinit var categoryAdapter: StoryAdapter
    private lateinit var newAdapter: StoryAdapter
    private lateinit var updatedAdapter: StoryAdapter
    private lateinit var completedAdapter: StoryAdapter
    private lateinit var recyclerViewTop: RecyclerView
    private lateinit var topAdapter: StoryAdapter
    private lateinit var formatHelper: FormatHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Khởi tạo RecyclerView
        recyclerViewTop = view.findViewById(R.id.recyclerViewTop)
        recyclerViewNew = view.findViewById(R.id.recyclerViewNew)
        recyclerViewUpdated = view.findViewById(R.id.recyclerViewUpdated)
        recyclerViewCompleted = view.findViewById(R.id.recyclerViewCompleted)

        // Gán LinearLayoutManager cho các RecyclerView
        recyclerViewTop.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNew.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpdated.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCompleted.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Khởi tạo DBHelper
        dbHelper = DBHelper(requireContext())

        // Lấy tất cả truyện từ cơ sở dữ liệu
        val allStories = dbHelper.getAllStories()

        // Lấy tất cả thể loại từ cơ sở dữ liệu
        val allCategories = dbHelper.getCategories()

        val topStories = dbHelper.getTopViewedStories()

        topAdapter = StoryAdapter(topStories, allCategories, dbHelper) { story ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("story_id", story.id)
            startActivity(intent)
        }
        recyclerViewTop.adapter = topAdapter

        // Lọc các truyện theo trạng thái: "Mới đăng", "Mới cập nhật", "Truyện hoàn thành"
        newAdapter = StoryAdapter(allStories.filter { it.status == "Mới đăng" }, allCategories, dbHelper) { story ->
            // Chuyển đến DetailActivity
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("story_id", story.id)  // Chỉ truyền story.id
            startActivity(intent)
        }
        recyclerViewNew.adapter = newAdapter

        updatedAdapter = StoryAdapter(allStories.filter { it.status == "Mới cập nhật" }, allCategories, dbHelper) { story ->
            // Chuyển đến DetailActivity
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("story_id", story.id)  // Chỉ truyền story.id
            startActivity(intent)
        }
        recyclerViewUpdated.adapter = updatedAdapter

        completedAdapter = StoryAdapter(allStories.filter { it.status == "Truyện hoàn thành" }, allCategories, dbHelper) { story ->
            // Chuyển đến DetailActivity
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("story_id", story.id)  // Chỉ truyền story.id
            startActivity(intent)
        }
        recyclerViewCompleted.adapter = completedAdapter

        return view
    }
}
