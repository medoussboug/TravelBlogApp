package com.example.travelblog

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelblog.adapter.MainAdapter
import com.example.travelblog.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = MainAdapter { blog ->
        BlogDetailsActivity.start(this, blog)
    }

    companion object {
        private const val SORT_TITLE = 0 // 1
        private const val SORT_DATE = 1 // 1
    }

    private var currentSort = SORT_DATE // 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.materialToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.sort) {
                onSortClicked() // implemented later in this lesson
            }
            false
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.refresh.setOnRefreshListener {
            loadData()
        }
        val searchItem = binding.materialToolbar.menu.findItem(R.id.search) // 1
        val searchView = searchItem.actionView as SearchView // 2
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener { // 3
            override fun onQueryTextSubmit(query: String): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText) // 4
                return true
            }
        })

        loadData()
    }

    private fun onSortClicked() {
        val items = arrayOf("Title", "Date")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort order")
            .setSingleChoiceItems(items, currentSort) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                currentSort = which
                sortData()
            }.show()
    }

    private fun sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle() // implemented later in this lesson
        } else if (currentSort == SORT_DATE) {
            adapter.sortByDate() // implemented later in this lesson
        }
    }

    private fun loadData() {
        binding.refresh.isRefreshing = true
        BlogHttpClient.loadArticles(
            onSuccess = { blogs: List<Blog> ->
                runOnUiThread {
                    binding.refresh.isRefreshing = false
                    adapter.setData(blogs)
                    adapter.submitList(blogs)
                }
            },
            onError = {
                runOnUiThread {
                    binding.refresh.isRefreshing = false
                    showSnackBar()
                }
            }
        )
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, "Error when loading data", Snackbar.LENGTH_INDEFINITE)
            .run {
                setActionTextColor(resources.getColor(R.color.orange500))
                setAction("Retry") {
                    loadData()
                    dismiss()
                }
            }.show()
    }
}