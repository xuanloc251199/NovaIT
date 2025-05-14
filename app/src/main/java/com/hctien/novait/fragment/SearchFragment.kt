package com.hctien.novait.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hctien.novait.R
import com.hctien.novait.activity.client.DetailActivity
import com.hctien.novait.adapter.StoryAdapter
import com.hctien.novait.data.DBHelper

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var dbHelper: DBHelper
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var searchTypeSpinner: Spinner
    private lateinit var recyclerViewSearchResults: RecyclerView
    private lateinit var searchAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        searchTypeSpinner = view.findViewById(R.id.searchTypeSpinner)
        recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults)

        dbHelper = DBHelper(requireContext())

        val searchOptions = arrayOf("Tên", "Tác giả", "Thể loại")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, searchOptions)
        searchTypeSpinner.adapter = adapter

        recyclerViewSearchResults.layoutManager = GridLayoutManager(requireContext(), 2)

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            val searchType = searchTypeSpinner.selectedItem.toString()

            val searchResults = when (searchType) {
                "Tên" -> dbHelper.searchStoriesByTitle(searchText)
                "Tác giả" -> dbHelper.searchStoriesByAuthor(searchText)
                "Thể loại" -> dbHelper.searchStoriesByCategory(searchText)
                else -> emptyList()
            }

            searchAdapter = StoryAdapter(searchResults, dbHelper.getCategories(), dbHelper) { story ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("story_id", story.id)
                startActivity(intent)
            }
            recyclerViewSearchResults.adapter = searchAdapter
        }

        return view
    }
}
