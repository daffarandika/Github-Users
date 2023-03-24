package com.example.githubuser.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class GithubUserEntity(

    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("login")
    @field:ColumnInfo("login")
    val login: String,

    @field:SerializedName("url")
    @field:ColumnInfo("url")
    val url: String,

    @field:SerializedName("avatar_url")
    @field:ColumnInfo("avatarUrl")
    val avatarUrl: String,

    @field:SerializedName("name")
    @field:ColumnInfo("name")
    val name: String,

    @field:SerializedName("followers")
    @field:ColumnInfo("followers")
    val followers: Int,

    @field:SerializedName("following")
    @field:ColumnInfo("following")
    val following: Int,

    @field:SerializedName("bio")
    @field:ColumnInfo("bio")
    val bio: String,

    @field:ColumnInfo("isFavorite")
    val isFavorite: Boolean,

): Parcelable
