package com.example.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.model.local.GithubUserEntity
import com.example.githubuser.model.local.GithubUserFollowers
import com.example.githubuser.model.local.GithubUserFollowing

@Database(
    entities = [
        GithubUserEntity::class,
        GithubUserFollowers::class,
        GithubUserFollowing::class
    ],
    version = 1
)
abstract class GithubUserDatabase: RoomDatabase() {
    abstract fun getDao(): GithubUserDao
    companion object {
        @Volatile
        private var INSTANCE: GithubUserDatabase? = null
        fun getInstance(context: Context): GithubUserDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserDatabase::class.java,
                    "GithubUserDB"
                ).build()
            }
    }
}