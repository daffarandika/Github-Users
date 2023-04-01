package com.example.githubuser.viewmodel

import androidx.lifecycle.*
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.model.Result
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader
import com.example.githubuser.networking.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: GithubUserRepository): ViewModel() {

    private var _githubUser = MutableLiveData<GithubUserDetailEntity>()
    val githubUser: LiveData<GithubUserDetailEntity> = _githubUser

    private var _githubFollowers = MutableLiveData<Result<List<GithubUserHeader>>>()
    val githubFollowers: LiveData<Result<List<GithubUserHeader>>> = _githubFollowers

    private var _githubFollowings = MutableLiveData<Result<List<GithubUserHeader>>>()
    val githubFollowings: LiveData<Result<List<GithubUserHeader>>> = _githubFollowings

    fun getFollowers(username: String) {
        viewModelScope.launch {
            _githubFollowers.value = Result.Success(ApiConfig.getApiService().getFollowers(username).map { user ->
                GithubUserHeader(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    isFavorite = isUserFavorite(user.login)
                )
            })
        }
    }

    suspend fun isUserFavorite(login: String): Boolean = repo.isUserFavorite(login)

    fun unsetUserAsFavorite (login: String){
        viewModelScope.launch {
            repo.unsetUserAsFavorite(login)
        }
    }
    fun setUserAsFavorite (login: String){
        viewModelScope.launch {
            repo.setUserAsFavorite(login)
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            _githubFollowings.value = Result.Success(ApiConfig.getApiService().getFollowings(username).map { user ->
                GithubUserHeader(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    isFavorite = isUserFavorite(user.login)
                )
            })
        }
    }

    fun getUserDetail(login: String): LiveData<Result<GithubUserDetailEntity>> = liveData {
        emit(Result.Loading)
        try {
            val user = ApiConfig.getApiService().getUserDetail(login)
            repo.insertUserDetail(
                GithubUserDetailEntity(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    name = user.name,
                    followers = user.followers,
                    following = user.following,
                    url =  user.url,
                    bio = user.bio
                )
            )
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        emitSource(repo.getUserDetail(login))
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}