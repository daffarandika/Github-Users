package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuser.model.Result
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.networking.ApiService

class GithubUserRepository private constructor(
    private val apiService: ApiService,
    private val githubUserDao: GithubUserDao
) {
    fun getFavoriteUsers(): LiveData<Result<List<GithubUserHeader>>> = liveData {
        emit(Result.Loading)
        try {
            val favoriteUsers: LiveData<Result<List<GithubUserHeader>>> = githubUserDao.getFavoriteUsers().map {
                Result.Success(it)
            }
            emitSource(favoriteUsers)
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    fun getAllHeader(): LiveData<Result<List<GithubUserHeader>>> = liveData{
        emit(Result.Loading)
        try {
            emitSource(githubUserDao.getUserHeaders().map {
                Result.Success(it)
            })
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    fun getInitialUser(): LiveData<Result<List<GithubUserHeader>>> = liveData{
        emit(Result.Loading)
        try {
            val githubUser = apiService.getInitialUsers()
            val usersList = githubUser.map {user ->
                GithubUserHeader(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    isFavorite = githubUserDao.isUserFavorite(user.login)
                )
            }
            githubUserDao.insertUserHeaders(usersList)
        } catch (e: java.lang.Exception){
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<GithubUserHeader>>> = githubUserDao.getUserHeaders().map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    suspend fun insertUsers(users: List<GithubUserHeader>) {
        githubUserDao.insertUserHeaders(users)
    }

    suspend fun searchUser(query: String): List<GithubUserHeader> {
        return apiService.searchUser(query).items.map { user ->
            val isUserFavorite = isUserFavorite(user.login)
            GithubUserHeader(
                login = user.login,
                avatarUrl = user.avatarUrl,
                isFavorite = isUserFavorite
            )
        }
    }

    suspend fun insertUserDetail(githubUserDetailEntity: GithubUserDetailEntity){
        githubUserDao.insertUserDetail(githubUserDetailEntity)
    }

    suspend fun isUserFavorite(login: String) = githubUserDao.isUserFavorite(login)

    suspend fun setUserAsFavorite(login: String) = githubUserDao.setUserAsFavorite(login)

    suspend fun unsetUserAsFavorite(login: String) = githubUserDao.unsetUserAsFavorite(login)

    fun getUserDetail(login: String): LiveData<Result<GithubUserDetailEntity>> = liveData{
        emit(Result.Loading)
        try {
            val localData: LiveData<Result<GithubUserDetailEntity>> = githubUserDao.getUserDetail(login).map { user ->
                Result.Success(user)
            }
            emitSource(localData)
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: GithubUserRepository? = null
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