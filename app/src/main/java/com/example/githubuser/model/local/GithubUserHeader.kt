package com.example.githubuser.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class GithubUserHeader(
    @PrimaryKey(autoGenerate = false)
    var login: String,
    var avatarUrl: String,
    var isFavorite: Boolean,
): Parcelable
