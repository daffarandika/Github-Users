package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.model.GithubSearchResponse
import com.example.githubuser.model.GithubUser
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.networking.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.githubuser.model.Result as Result

class SearchViewModel: ViewModel() {
    
    private val _githubUsers = MutableLiveData<Result<List<GithubUser>>>()
    val githubUsers: LiveData<Result<List<GithubUser>>> = _githubUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _isUsingLinearLayout = MutableLiveData<Boolean>()
    val isUsingLinearLayout: LiveData<Boolean> = _isUsingLinearLayout

    init {
        _isUsingLinearLayout.value = true
        getInitialUsers()
    }
    fun getInitialUsers(){
        viewModelScope.launch {
            try {
                _githubUsers.value = Result.Success(ApiConfig.getApiService().getInitialUsers())
            } catch (e: Exception) {
                _githubUsers.value = Result.Error(e.message.toString())
            }
        }
    }
    fun setLayoutManager(isUsingLinearLayout: Boolean) {
        _isUsingLinearLayout.value = isUsingLinearLayout
    }

    fun searchUser(query: String) {
        viewModelScope.launch {
            try {
                val users = ApiConfig.getApiService().searchUser(query).items
                _githubUsers.value = Result.Success(users)
            } catch (e: java.lang.Exception) {
                _githubUsers.value = Result.Error(e.message.toString())
            }
        }
    }
    companion object {
        private const val TAG = "SearchViewModel"
    }
}