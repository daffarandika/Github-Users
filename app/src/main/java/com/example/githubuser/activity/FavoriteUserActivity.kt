package com.example.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuser.R
import com.example.githubuser.database.FavoriteUserDatabase
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.model.FavoriteUser

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = FavoriteUserDatabase.getInstance(this)
        // val favoriteUsers =

    }
}