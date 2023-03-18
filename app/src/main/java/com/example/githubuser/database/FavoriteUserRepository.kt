package com.example.githubuser.database

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

class FavoriteUserRepository(app: Application) {
    private val favoriteUserDao: FavoriteUserDao
    init {
        val db = FavoriteUserDatabase.getInstance(app)
        favoriteUserDao = db.dao()
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = liveData{
        emitSource(favoriteUserDao.getFavoriteUsers())
    }

    suspend fun addNewFavoriteUser(favoriteUser: FavoriteUser) {
        favoriteUserDao.addNewFavoriteUser(favoriteUser)
    }

    fun isUserFavorite(login: String): LiveData<Boolean> = liveData {
        emit(favoriteUserDao.isUserFavorite(login))
    }
}