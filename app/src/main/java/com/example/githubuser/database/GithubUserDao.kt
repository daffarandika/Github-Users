package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader

@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserHeaders(githubUserHeaders: List<GithubUserHeader>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(githubUserDetailEntity: GithubUserDetailEntity)

    @Query("SELECT * FROM GithubUserHeader")
    fun getUserHeaders(): LiveData<List<GithubUserHeader>>

    @Query("SELECT * FROM GithubUserDetailEntity")
    fun getUserDetails(): LiveData<List<GithubUserDetailEntity>>

    @Query("SELECT EXISTS (SELECT login FROM githubUserDetailEntity where login = :login and isFavorite = 1)")
    suspend fun isUserFavorite(login: String): Boolean


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertGithubUsers(users: List<GithubUserEntity>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertFollowers(followers: List<GithubUserFollowers>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertFollowing(following: List<GithubUserFollowing>)
//    @Update
//    suspend fun updateGithubUserEntity(githubUserEntity: GithubUserEntity)
//
//    @Query("SELECT EXISTS (SELECT login FROM githubUserEntity where login = :login and isFavorite = 1)")
//    suspend fun isUserFavorite(login: String): Boolean
//
//    @Query("SELECT * FROM githubUserEntity where isFavorite = 1")
//    fun getAllFavoriteUser(): LiveData<List<GithubUserEntity>>
//
//    @Query("SELECT * FROM githubUserEntity")
//    fun getAllUsers(): LiveData<List<GithubUserEntity>>
//
//    @Transaction
//    @Query("SELECT * FROM githubUserEntity where login = :githubUserLogin")
//    fun getUserFollowers(githubUserLogin: String): LiveData<List<GithubUserWithFollowers>>
//
//    @Transaction
//    @Query("SELECT * FROM githubUserEntity where login = :githubUserLogin")
//    fun getUserFollowing(githubUserLogin: String): LiveData<List<GithubUserWithFollowing>>

}