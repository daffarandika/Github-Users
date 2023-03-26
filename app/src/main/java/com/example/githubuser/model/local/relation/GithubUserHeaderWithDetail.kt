package com.example.githubuser.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader

data class GithubUserHeaderWithDetail (
    @Embedded val githubUserHeader: GithubUserHeader,
    @Relation(
        parentColumn = "login",
        entityColumn = "login"
    )
    val githubUserDetailEntity: GithubUserDetailEntity
)