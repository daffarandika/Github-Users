package com.example.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.FavoriteUserAdapter
import com.example.githubuser.database.FavoriteUserDatabase
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.model.FavoriteUser
import com.example.githubuser.viewmodel.FavoriteUserViewModel
import com.example.githubuser.viewmodel.createFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = FavoriteUserDatabase.getInstance(this@FavoriteUserActivity)
        val factory = FavoriteUserViewModel(db.favUserDao()).createFactory()
        val viewModel = ViewModelProvider(this, factory)[FavoriteUserViewModel::class.java]
        val adapter = FavoriteUserAdapter()
        viewModel.getFavoriteUsers().observe(this) {users ->
            adapter.submitList(users)
            binding.rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            binding.rvFavoriteUser.adapter = adapter
        }

    }
}