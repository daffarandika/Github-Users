package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.model.GithubSearchResponse
import com.example.githubuser.model.GithubUser
import com.example.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {
    
    private val _githubUsers = MutableLiveData<List<GithubUser>>()
    val githubUsers: LiveData<List<GithubUser>> = _githubUsers

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
    private fun getInitialUsers() {
        val githubUsers = mutableListOf<GithubUser>()
        val client = ApiConfig.getApiService().getInitialUsers()
        client.enqueue(object: Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>,
            ) {
                _isLoading.value = (true)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        for (user in responseBody) {
                            githubUsers.add(user)
                        }
                    }
                    _githubUsers.value = githubUsers
                    _isLoading.value = (false)
                }
                else Log.e(TAG, "onResponseFail: ${response.code()}")
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _isLoading.value = (false)
                Log.e(TAG, "onFailure: ${t.message} ")
            }

        })
    }
    fun setLayoutManager(isUsingLinearLayout: Boolean) {
        _isUsingLinearLayout.value = isUsingLinearLayout
    }

    fun searchUser(query: String) {
        val githubUsers = mutableListOf<GithubUser>()
        val client = ApiConfig.getApiService().searchUser(query)
        client.enqueue(object: Callback<GithubSearchResponse>{

            override fun onResponse(
                call: Call<GithubSearchResponse>,
                response: Response<GithubSearchResponse>,
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val users = responseBody.items
                        for (user in users){
                            githubUsers.add(user)
                        }
                        _githubUsers.value = githubUsers
                    } else {
                        Log.e(TAG, "onResponse: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GithubSearchResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
    companion object {
        private const val TAG = "SearchViewModel"
    }
}