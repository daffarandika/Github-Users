package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.model.GithubUser

@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGithubUsers(users: List<GithubUser>)

    @Query("SELECT * FROM githubuser")
    fun getAllUsers(): LiveData<List<GithubUser>>

}