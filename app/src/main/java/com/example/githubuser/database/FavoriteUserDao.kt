package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.githubuser.model.FavoriteUser

@Dao
interface FavoriteUserDao {

    @Upsert
    suspend fun addFavoriteUser(fav: FavoriteUser)

    @Delete
    suspend fun removeFavoriteUser(fav: FavoriteUser)

    @Query("select if exists (select * from favoriteUser where login = :login)")
    fun isUserFavorite(login: String): Boolean

    @Query("select * from favoriteUser")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

}