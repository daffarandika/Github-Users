package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.model.local.GithubUserEntity
import com.example.githubuser.model.local.GithubUserFollowers
import com.example.githubuser.model.local.GithubUserFollowing
import com.example.githubuser.model.local.relation.GithubUserWithFollowers
import com.example.githubuser.model.local.relation.GithubUserWithFollowing

@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGithubUsers(users: List<GithubUserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFollowers(followers: List<GithubUserFollowers>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFollowing(following: List<GithubUserFollowing>)
    @Update
    suspend fun updateGithubUserEntity(githubUserEntity: GithubUserEntity)

    @Query("SELECT EXISTS (SELECT login FROM githubUserEntity where login = :login and isFavorite = 1)")
    suspend fun isUserFavorite(login: String): Boolean

    @Query("SELECT * FROM githubUserEntity where isFavorite = 1")
    fun getAllFavoriteUser(): LiveData<List<GithubUserEntity>>

    @Query("SELECT * FROM githubUserEntity")
    fun getAllUsers(): LiveData<List<GithubUserEntity>>

    @Transaction
    @Query("SELECT * FROM githubUserEntity where login = :githubUserLogin")
    fun getUserFollowers(githubUserLogin: String): LiveData<List<GithubUserWithFollowers>>

    @Transaction
    @Query("SELECT * FROM githubUserEntity where login = :githubUserLogin")
    fun getUserFollowing(githubUserLogin: String): LiveData<List<GithubUserWithFollowing>>

}