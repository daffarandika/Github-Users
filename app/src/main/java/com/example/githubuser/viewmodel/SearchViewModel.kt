package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.model.GithubSearchResponse
import com.example.githubuser.model.GithubUser
import com.example.githubuser.model.local.GithubUserEntity
import com.example.githubuser.networking.ApiConfig
import kotlinx.coroutines.launch
import kotlin.math.log
import com.example.githubuser.model.Result as Result

class SearchViewModel(private val githubUserRepository: GithubUserRepository): ViewModel() {
    
    private val _githubUsers = MutableLiveData<Result<List<GithubUserEntity>>>()
    val githubUsers: LiveData<Result<List<GithubUserEntity>>> = _githubUsers

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    init {
        getInitialUsers()
    }
    fun getInitialUsers(): LiveData<Result<List<GithubUserEntity>>> = githubUserRepository.getAllUsers()

    fun getAllFavoriteUser() = githubUserRepository.getFavoriteUser()

    fun setUserAsFavorite(user: GithubUserEntity){
        viewModelScope.launch {
            githubUserRepository.setUserFavorite(user, true)
        }
    }

    fun unsetUserAsFavorite(user: GithubUserEntity){
        viewModelScope.launch {
            githubUserRepository.setUserFavorite(user, false)
        }
    }

    fun searchUser(query: String) {
        viewModelScope.launch {
            try {
                val users = ApiConfig.getApiService().searchUser(query).items.map { user ->
                    GithubUserEntity(
                        login = user.login,
                        avatarUrl = user.avatarUrl,
                        url = user.url,
                        isFavorite = githubUserRepository.isUserFavorite(user.login),
                        bio = "",
                        followers = -1,
                        following = -1,
                        name = ""
                    )
                }
                _githubUsers.value = Result.Success(users)
            } catch (e: java.lang.Exception) {
                _githubUsers.value = Result.Error(e.message.toString())
            }
        }
    }

    fun setUsers(users: Result<List<GithubUserEntity>>){
        _githubUsers.value = users
    }
    companion object {
        private const val TAG = "SearchViewModel"
    }
}