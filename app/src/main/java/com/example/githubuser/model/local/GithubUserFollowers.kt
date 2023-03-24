package com.example.githubuser.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GithubUserFollowers(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val githubUserLogin: String,
    val followersLogin: String
)
