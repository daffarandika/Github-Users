package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.model.GithubUser
import com.example.githubuser.model.GithubUserDetail
import com.example.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    var _githubUser = MutableLiveData<GithubUserDetail>()
    val githubUser: LiveData<GithubUserDetail> = _githubUser

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
                        _githubFollowers.value = responseBody
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
                        _githubFollowings.value = responseBody
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

    fun getDetail(username: String) {
        val clientDetail = ApiConfig.getApiService().getUserDetail(username!!)
        clientDetail.enqueue(object: Callback<GithubUserDetail>{
            override fun onResponse(
                call: Call<GithubUserDetail>,
                response: Response<GithubUserDetail>,
            ) {
                val responseBody = response.body()
                _isLoading.value = (true)
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        _githubUser.value = responseBody
                        _isLoading.value = (false)
                    }
                    else Log.e(TAG, "onResponse: ${response.message()}")
                } else Log.e(TAG, "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<GithubUserDetail>, t: Throwable) {
                _isLoading.value = (false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }
    companion object {
        private const val TAG = "DetailViewModel"
    }
}