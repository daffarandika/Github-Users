package com.example.githubuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.model.GithubUser
import com.example.githubuser.model.GithubUserDetail

class DetailViewModel: ViewModel() {
    private lateinit var githubUser: GithubUserDetail


    fun setGithubUser(user: GithubUserDetail) {
        githubUser = user
    }
}