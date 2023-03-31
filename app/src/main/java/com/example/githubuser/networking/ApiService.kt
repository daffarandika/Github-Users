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
    suspend fun getUserDetail(
        @Path("username") username : String
    ): GithubUserDetail

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<GithubUser>

    @GET("users/{username}/following")
    suspend fun getFollowings(
        @Path("username") username: String
    ): List<GithubUser>
}