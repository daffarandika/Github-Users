package com.example.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import com.example.githubuser.database.FavoriteUserRepository

class FavoriteUserViewModel(private val repo: FavoriteUserRepository): ViewModel() {
    fun getFavoriteUsers() = repo.getFavoriteUsers()
}