package com.example.githubuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GithubDefaultResponse(

	@field:SerializedName("items")
	val items: List<GithubUser>
)

data class GithubSearchResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<GithubUser>
)

@Entity
@Parcelize
data class GithubUser(

	@field:ColumnInfo("login")
	@field:PrimaryKey
	val login: String,

	@field:ColumnInfo("url")
	val url: String,

	@field:ColumnInfo("avatar_url")
	val avatarUrl: String,

	@field:ColumnInfo("is_favorite")
	val isFavorite: Boolean,

): Parcelable
data class GithubUserDetail(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("bio")
	val bio: String,
): java.io.Serializable
