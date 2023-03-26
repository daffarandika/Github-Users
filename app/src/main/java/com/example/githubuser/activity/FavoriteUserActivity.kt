package com.example.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserAdapter
import com.example.githubuser.database.GithubUserDatabase
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.model.Result
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.viewmodel.SearchViewModel
import com.example.githubuser.viewmodel.createFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val searchViewModelFactory = SearchViewModel(GithubUserRepository.getInstance(ApiConfig.getApiService(), GithubUserDatabase.getInstance(this).getDao())).createFactory()
        val searchViewModel = ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]
        val adapter = GithubUserAdapter{ user ->
//            if (user.isFavorite) {
//                searchViewModel.unsetUserAsFavorite(user)
//            } else {
//                searchViewModel.setUserAsFavorite(user)
//            }
        }
//        searchViewModel.getAllFavoriteUser().observe(this) {res ->
//            when (res) {
//                is Result.Loading -> {}
//                is Result.Error -> {
//                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
//                }
//                is Result.Success -> {
//                    adapter.submitList(res.data)
//                }
//            }
//        }
        binding.rvFav.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
        }
    }
}