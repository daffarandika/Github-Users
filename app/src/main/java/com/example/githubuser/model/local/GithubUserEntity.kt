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
    var login: String,

    @field:SerializedName("url")
    @field:ColumnInfo("url")
    var url: String,

    @field:SerializedName("avatar_url")
    @field:ColumnInfo("avatarUrl")
    var avatarUrl: String,

    @field:SerializedName("name")
    @field:ColumnInfo("name")
    var name: String,

    @field:SerializedName("followers")
    @field:ColumnInfo("followers")
    var followers: Int,

    @field:SerializedName("following")
    @field:ColumnInfo("following")
    var following: Int,

    @field:SerializedName("bio")
    @field:ColumnInfo("bio")
    var bio: String,

    @field:ColumnInfo("isFavorite")
    var isFavorite: Boolean,

): Parcelable
