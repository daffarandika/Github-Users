package com.example.githubuser.viewmodel

import androidx.lifecycle.*
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader
import com.example.githubuser.networking.ApiConfig
import kotlinx.coroutines.launch
import com.example.githubuser.model.Result as Result

class SearchViewModel(private val githubUserRepository: GithubUserRepository): ViewModel() {
    
    private val _githubUsers = MutableLiveData<Result<List<GithubUserHeader>>>()
    val githubUsers: LiveData<Result<List<GithubUserHeader>>> = _githubUsers

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    init {
        getInitialUsers()
    }
    fun getInitialUsers(): LiveData<Result<List<GithubUserHeader>>> = githubUserRepository.getInitialUser()

    //fun getAllFavoriteUser() = githubUserRepository.getFavoriteUser()

//    fun setUserAsFavorite(user: GithubUserDetailEntity){
//        viewModelScope.launch {
//            githubUserRepository.setUserFavorite(user, true)
//        }
//    }
//
//    fun unsetUserAsFavorite(user: GithubUserEntity){
//        viewModelScope.launch {
//            githubUserRepository.setUserFavorite(user, false)
//        }
//    }

    fun insertUserDetail(login: String) {
        viewModelScope.launch {
            val user = ApiConfig.getApiService().getUserDetail(login)
            githubUserRepository.insertUserDetail(
                GithubUserDetailEntity(
                    login = login,
                    url = user.url,
                    name = user.name,
                    following = user.following,
                    followers = user.followers,
                    bio = user.bio ?: "",
                    isFavorite = false
                )
            )
        }
    }

    fun searchUser(query: String) {
        viewModelScope.launch {
            try {
                val users = ApiConfig.getApiService().searchUser(query).items.map { user ->
                    GithubUserHeader(
                        login = user.login,
                        avatarUrl = user.avatarUrl,
                    )
                }
                _githubUsers.value = Result.Success(users)
            } catch (e: java.lang.Exception) {
                _githubUsers.value = Result.Error(e.message.toString())
            }
        }
    }

    fun setUsers(users: Result<List<GithubUserHeader>>){
        _githubUsers.value = users
    }
    companion object {
        private const val TAG = "SearchViewModel"
    }
}