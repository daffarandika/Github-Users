package com.example.githubuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser (
    @PrimaryKey(autoGenerate = false)
    val login: String? = null,
    val avatarUrl: String? = null
)