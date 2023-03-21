package com.example.githubuser.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.githubuser.model.FavoriteUser

class FavoriteUserRepository constructor(
    val context: Context
) {

    val favoriteUserDao = FavoriteUserDatabase.getInstance(context).favUserDao()

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