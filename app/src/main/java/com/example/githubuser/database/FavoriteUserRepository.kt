package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.githubuser.model.FavoriteUser

class FavoriteUserRepository private constructor(
    private val favoriteUserDao: FavoriteUserDao
) {

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = liveData{
        emitSource(favoriteUserDao.getFavoriteUser())
    }

    fun isUserFavorite(login: String): Boolean {
        return favoriteUserDao.isUserFavorite(login)
    }

    suspend fun setFavoriteUser(favoriteUser: FavoriteUser) {
        favoriteUserDao.addFavoriteUser(favoriteUser)
    }

    suspend fun removeFavoriteUser(favoriteUser: FavoriteUser) {
        favoriteUserDao.removeFavoriteUser(favoriteUser)
    }

}