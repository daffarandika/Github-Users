package com.example.githubuser.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class GithubUserHeader(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("login")
    @field:ColumnInfo("login")
    var login: String,

    @field:SerializedName("avatar_url")
    @field:ColumnInfo("avatarUrl")
    var avatarUrl: String,
): Parcelable
