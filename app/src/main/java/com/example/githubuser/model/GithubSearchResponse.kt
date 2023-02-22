package com.example.githubuser.model

import com.google.gson.annotations.SerializedName

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

data class GithubUser(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

)
