package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.model.GithubUser
import com.example.githubuser.model.GithubUserDetail

class DetailViewModel: ViewModel() {
    var _githubUser = MutableLiveData<GithubUserDetail>()
    val githubUser: LiveData<GithubUserDetail> = _githubUser

    var _githubFollowers = MutableLiveData<List<GithubUser>>()
    val githubFollowers: LiveData<List<GithubUser>> = _githubFollowers

    var _githubFollowings = MutableLiveData<List<GithubUser>>()
    val githubFollowings: LiveData<List<GithubUser>> = _githubFollowings

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
}