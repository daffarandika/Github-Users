package com.example.githubuser.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserFollowing

data class GithubUserWithFollowing (
    @Embedded val githubUserEntity: GithubUserDetailEntity,
    @Relation(
        parentColumn = "login",
        entityColumn = "githubUserLogin"
    )
    val following: List<GithubUserFollowing>
)