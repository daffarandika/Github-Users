package com.example.githubuser.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.fragment.SettingDialogFragment
import com.example.githubuser.preferences.SettingPreference
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.SearchViewModel
import com.example.githubuser.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.switch_layout) {
            searchViewModel.setLayoutManager(!searchViewModel.isUsingLinearLayout.value!!)
            setIcon(item)
        }
        if (item.itemId == R.id.setting) {
            Intent(this@MainActivity, SettingActivity::class.java).apply {
                startActivity(this)
            }
        }
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

        val pref = SettingPreference.getInstance(dataStore)

        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )
        mainViewModel.getThemeSetting().observe(this) {isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        searchViewModel.isLoading.observe(this) {isLoading ->
            showLoading(isLoading)
        }

        searchViewModel.githubUsers.observe(this) { users ->
            binding.rv.apply {
                adapter = if (searchViewModel.isUsingLinearLayout.value!!) {
                    GithubUserAdapter(R.layout.item_github_user_linear, users, this@MainActivity)
                } else {
                    GithubUserAdapter(R.layout.item_github_user_grid, users, this@MainActivity)
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
                        GithubUserAdapter(R.layout.item_github_user_linear, users, this@MainActivity)
                    } else {
                        GithubUserAdapter(R.layout.item_github_user_grid, users, this@MainActivity)
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