package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuser.model.local.GithubUserEntity
import com.example.githubuser.networking.ApiService

class GithubUserRepository private constructor(
    private val apiService: ApiService,
    private val githubUserDao: GithubUserDao
) {

    suspend fun addDetailToUser(githubUserEntity: GithubUserEntity) {
        githubUserDao.updateGithubUserEntity(githubUserEntity)
    }

    fun getAllUsers(): LiveData<com.example.githubuser.model.Result<List<GithubUserEntity>>> = liveData {
        emit(com.example.githubuser.model.Result.Loading)
        try {
            val githubUser = apiService.getInitialUsers()
            val usersList = githubUser.map {user ->
                GithubUserEntity(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    url = user.url,
                    isFavorite = false,
                    bio = "",
                    followers = -1,
                    following = -1,
                    name = ""
                )
            }
            githubUserDao.upsertGithubUsers(usersList)
        } catch (e: java.lang.Exception) {
            emit(com.example.githubuser.model.Result.Error(e.message.toString()))
        }
        val localData: LiveData<com.example.githubuser.model.Result<List<GithubUserEntity>>> = githubUserDao.getAllUsers().map {
            com.example.githubuser.model.Result.Success(it)
        }
        emitSource(localData)
    }

    companion object {
        @Volatile
        var INSTANCE: GithubUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            githubUserDao: GithubUserDao
        ): GithubUserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: GithubUserRepository(apiService, githubUserDao)
            }.also{
                INSTANCE = it
            }
    }
}