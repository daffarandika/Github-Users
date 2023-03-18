package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {

    @Upsert
    suspend fun addNewFavoriteUser(favoriteUser: FavoriteUser)

    @Delete
    suspend fun removeFavoriteUser(favoriteUser: FavoriteUser)

    @Query("select * from favoriteUser")
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("select exists(select * from favoriteUser where login = :login)")
    fun isUserFavorite(login: String): Boolean
}