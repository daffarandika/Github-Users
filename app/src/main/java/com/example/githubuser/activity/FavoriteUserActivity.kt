package com.example.githubuser.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.model.Result
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.preferences.SettingPreference
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.SearchViewModel
import com.example.githubuser.viewmodel.createFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            startActivity(Intent(this@FavoriteUserActivity, SettingActivity::class.java))
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        supportActionBar?.title = "Favorite Users"
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
        val searchViewModelFactory = SearchViewModel(GithubUserRepository.getInstance(ApiConfig.getApiService(), GithubUserDatabase.getInstance(this).getDao())).createFactory()
        val searchViewModel = ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]
        val adapter = GithubUserAdapter(onHeartClick = { user ->
            if (user.isFavorite) {
                searchViewModel.unsetUserAsFavorite(user.login)
            } else {
                searchViewModel.setUserAsFavorite(user.login)
            }
        }, onItemClick = { user ->
            startActivity(Intent(
                this@FavoriteUserActivity,
                DetailActivity::class.java).apply {
                    putExtra("username", user.login)
            })
        })
        searchViewModel.getAllFavoriteUsers().observe(this) {res ->
            when (res) {
                is Result.Loading -> {
                    binding.tvNoUser.visibility = View.INVISIBLE
                }
                is Result.Error -> {
                    binding.tvNoUser.visibility = View.INVISIBLE
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    adapter.submitList(res.data)
                    if (res.data.isEmpty()) {
                        binding.tvNoUser.visibility = View.VISIBLE
                    } else {
                        binding.tvNoUser.visibility = View.INVISIBLE
                    }
                }
            }
        }
        binding.rvFav.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
        }
        binding.fabSearch.setOnClickListener {
            startActivity(Intent(this@FavoriteUserActivity, SearchActivity::class.java))
        }
    }
}