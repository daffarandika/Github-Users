package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.adapter.GithubUserAdapterGrid
import com.example.githubuser.adapter.GithubUserAdapterLinear
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val searchViewModel by viewModels<SearchViewModel>()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.switch_layout) {
            searchViewModel.setLayoutManager(!searchViewModel.isUsingLinearLayout.value!!)
        }
        setIcon(item)
        return true
    }

    private fun setIcon(item: MenuItem) {
        item.icon = if (!searchViewModel.isUsingLinearLayout.value!!)
                        ContextCompat.getDrawable(this, R.drawable.ic_linear_layout)
                    else
                        ContextCompat.getDrawable(this, R.drawable.ic_grid_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchViewModel.isLoading.observe(this) {isLoading ->
            showLoading(isLoading)
        }

        searchViewModel.githubUsers.observe(this) { users ->
            binding.rv.apply {
                adapter = if (searchViewModel.isUsingLinearLayout.value!!) {
                    GithubUserAdapterLinear(users, this@MainActivity)
                } else {
                    GithubUserAdapterGrid(users, this@MainActivity)
                }
            }
            searchViewModel.isUsingLinearLayout.observe(this) {isUsing ->
                layoutManager = getLayoutManager(isUsing)
                binding.rv.layoutManager = this.layoutManager
            }
        }

        searchViewModel.isUsingLinearLayout.observe(this) {isUsing ->
            layoutManager = getLayoutManager(isUsing)
            binding.rv.layoutManager = this.layoutManager
            searchViewModel.githubUsers.observe(this) { users ->
                binding.rv.apply {
                    adapter = if (searchViewModel.isUsingLinearLayout.value!!) {
                        GithubUserAdapterLinear(users, this@MainActivity)
                    } else {
                        GithubUserAdapterGrid(users, this@MainActivity)
                    }
                }
            }
        }

        binding.svUsers.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.searchUser(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
    }

    private fun getLayoutManager(isUsing: Boolean): RecyclerView.LayoutManager {
        return if (isUsing) {
            LinearLayoutManager(this@MainActivity)
        } else {
            GridLayoutManager(this@MainActivity, 3)
        }
    }


    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

}