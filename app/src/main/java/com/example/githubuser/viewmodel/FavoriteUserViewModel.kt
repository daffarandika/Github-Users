package com.example.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import com.example.githubuser.database.FavoriteUserDao

class FavoriteUserViewModel(private val favoriteUserDao: FavoriteUserDao): ViewModel() {
    fun getFavoriteUsers() = favoriteUserDao.getFavoriteUser()
}