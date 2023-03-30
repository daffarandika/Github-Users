package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.model.GithubUser
import com.example.githubuser.model.GithubUserDetail
import com.example.githubuser.model.Result
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(val repo: GithubUserRepository): ViewModel() {
    var _githubUser = MutableLiveData<GithubUserDetailEntity>()
    val githubUser: LiveData<GithubUserDetailEntity> = _githubUser

    var _githubFollowers = MutableLiveData<List<GithubUser>>()
    val githubFollowers: LiveData<List<GithubUser>> = _githubFollowers

    var _githubFollowings = MutableLiveData<List<GithubUser>>()
    val githubFollowings: LiveData<List<GithubUser>> = _githubFollowings

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowers(username: String) {
        val clientFollowers = ApiConfig.getApiService().getFollowers(username)
        clientFollowers.enqueue(object: Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>,
            ) {
                _isLoading.value = true
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                        _githubFollowers.value = responseBody!!
                        _isLoading.value = false
                    } else Log.e(TAG, "onResponse: ${response.message()}")
                } else Log.e(TAG, "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    suspend fun isUserFavorite(login: String): Boolean = repo.isUserFavorite(login)

    fun getFollowing(username: String) {
        val clientFollowings = ApiConfig.getApiService().getFollowings(username)
        clientFollowings.enqueue(object: Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>,
            ) {
                _isLoading.value = true
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                        _githubFollowings.value = responseBody!!
                        _isLoading.value = false
                    } else Log.e(TAG, "onResponse: ${response.message()}")
                } else Log.e(TAG, "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(login: String): LiveData<com.example.githubuser.model.Result<GithubUserDetailEntity>> = liveData {
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