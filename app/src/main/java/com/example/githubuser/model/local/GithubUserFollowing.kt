package com.example.githubuser.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GithubUserFollowing(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    val githubUserLogin: String,
    val followingLogin: String
)
