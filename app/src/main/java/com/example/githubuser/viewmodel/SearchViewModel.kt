package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.model.Result
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader
import com.example.githubuser.networking.ApiConfig
import kotlinx.coroutines.launch

class SearchViewModel(private val githubUserRepository: GithubUserRepository): ViewModel() {
    
    private val _githubUsers = MutableLiveData<Result<List<GithubUserHeader>>>()
    val githubUsers: LiveData<Result<List<GithubUserHeader>>> = _githubUsers

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun getAllFavoriteUsers() = githubUserRepository.getFavoriteUsers()

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
                    bio = user.bio,
                    avatarUrl = user.avatarUrl
                )
            )
        }
    }

    fun unsetUserAsFavorite (login: String){
        viewModelScope.launch {
            githubUserRepository.unsetUserAsFavorite(login)
        }
    }
    fun setUserAsFavorite (login: String){
        viewModelScope.launch {
            githubUserRepository.setUserAsFavorite(login)
        }
    }

    fun searchUser(query: String) {
        viewModelScope.launch {
            val users = githubUserRepository.searchUser(query)
            githubUserRepository.insertUsers(users)
        }
    }

    fun getAllHeaders() = githubUserRepository.getAllHeader()

    fun setSearchQuery(query: String){
        _searchQuery.value = query
    }

    fun setUsers(users: Result<List<GithubUserHeader>>){
        _githubUsers.value = users
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}