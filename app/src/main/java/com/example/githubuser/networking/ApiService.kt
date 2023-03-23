package com.example.githubuser.networking

import com.example.githubuser.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    suspend fun getInitialUsers(): List<GithubUser>

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") q: String
    ): GithubSearchResponse

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username : String
    ): Call<GithubUserDetail>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<GithubUser>>

    @GET("users/{username}/following")
    fun getFollowings(
        @Path("username") username: String
    ): Call<List<GithubUser>>
}