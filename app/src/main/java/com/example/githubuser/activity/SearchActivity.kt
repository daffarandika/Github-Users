package com.example.githubuser.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserAdapter
import com.example.githubuser.database.GithubUserDatabase
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.databinding.ActivitySearchBinding
import com.example.githubuser.model.local.GithubUserHeader
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.preferences.SettingPreference
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.SearchViewModel
import com.example.githubuser.viewmodel.createFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            startActivity(Intent(
                this@SearchActivity,
                SettingActivity::class.java))
        } else {
            finish()
        }
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val db = GithubUserDatabase.getInstance(this@SearchActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userRepo = GithubUserRepository.getInstance(
            ApiConfig.getApiService(),
            db.getDao()
        )
        val searchViewModelFactory = SearchViewModel(userRepo).createFactory()
        searchViewModel = ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]
        setContentView(binding.root)

        val pref = SettingPreference.getInstance(dataStore)
        val mainViewModelFactory = MainViewModel(pref).createFactory()
        val mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        mainViewModel.getThemeSetting().observe(this) {isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        searchViewModel.getInitialUsers().observe(this){res->
            searchViewModel.setUsers(res)
        }

        val adapter = GithubUserAdapter(onHeartClick = { user ->
            searchViewModel.insertUserDetail(user.login)
            if (user.isFavorite) {
                searchViewModel.unsetUserAsFavorite(user.login)
            } else {
                searchViewModel.setUserAsFavorite(user.login)
            }
        },
        onItemClick = { user ->
            searchViewModel.insertUserDetail(user.login)
            Intent(this@SearchActivity, DetailActivity::class.java).apply {
                putExtra("username", user.login)
                startActivity(this)
            }
        })

        searchViewModel.githubUsers.observe(this) {res ->
            when (res) {
                is com.example.githubuser.model.Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is com.example.githubuser.model.Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@SearchActivity, res.message, Toast.LENGTH_SHORT).show()
                }
                is com.example.githubuser.model.Result.Success -> {
                    if (res.data.isEmpty()) {
                        Toast.makeText(this@SearchActivity, "No users were found", Toast.LENGTH_SHORT).show()
                    }
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(res.data.map{user ->
                        GithubUserHeader(
                            login = user.login,
                            avatarUrl = user.avatarUrl,
                            isFavorite = user.isFavorite
                        )
                    })
                }
            }
        }

        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            this.adapter = adapter
        }

        binding.svUsers.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.searchUser(query).observe(this@SearchActivity) { res ->
                    when (res) {
                        is com.example.githubuser.model.Result.Loading -> {}
                        is com.example.githubuser.model.Result.Error -> {}
                        is com.example.githubuser.model.Result.Success -> {
                            adapter.submitList(res.data)
                            binding.rv.apply {
                                layoutManager = LinearLayoutManager(this@SearchActivity)
                                this.adapter = adapter
                            }
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // TODO: add this to viewmodel
                return false
            }
        })

    }

}