package com.example.githubuser.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    val login: String = "",
    val avatarUrl: String? = null
)