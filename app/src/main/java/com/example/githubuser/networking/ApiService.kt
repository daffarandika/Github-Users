package com.example.githubuser.networking

import com.example.githubuser.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getInitialUsers(): Call<List<GithubUser>>
    @GET("search/users")
    fun searchUser(
        @Query("q") q: String
    ): Call<GithubSearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username : String
    ): Call<GithubUserDetail>
}