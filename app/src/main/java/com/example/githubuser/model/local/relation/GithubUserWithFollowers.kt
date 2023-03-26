package com.example.githubuser.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserFollowers

data class GithubUserWithFollowers (
    @Embedded val githubUserEntity: GithubUserDetailEntity,
    @Relation(
        parentColumn = "login",
        entityColumn = "followersLogin"
    )
    val followers: List<GithubUserFollowers>
)