package com.example.githubuser.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class GithubUserDetailEntity(

    @PrimaryKey(autoGenerate = false)
    var login: String,
    var url: String,
    var name: String?,
    var followers: Int,
    var following: Int,
    var bio: String?,
    var avatarUrl: String

): Parcelable
