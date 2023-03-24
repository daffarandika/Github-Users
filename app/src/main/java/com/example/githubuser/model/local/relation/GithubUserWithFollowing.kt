package com.example.githubuser.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.githubuser.model.local.GithubUserEntity
import com.example.githubuser.model.local.GithubUserFollowing

data class GithubUserWithFollowing (
    @Embedded val githubUserEntity: GithubUserEntity,
    @Relation(
        parentColumn = "login",
        entityColumn = "githubUserLogin"
    )
    val followers: List<GithubUserFollowing>
)