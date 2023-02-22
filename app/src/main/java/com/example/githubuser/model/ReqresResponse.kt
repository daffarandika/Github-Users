package com.example.githubuser.model

import com.google.gson.annotations.SerializedName

data class ReqresResponse(

	@field:SerializedName("data")
	val data: List<UserItem>,

)

data class UserItem(

	@field:SerializedName("last_name")
	val lastName: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("first_name")
	val firstName: String,

	@field:SerializedName("email")
	val email: String
)
