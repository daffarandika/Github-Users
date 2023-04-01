package com.example.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader

@Database(
    entities = [
        GithubUserDetailEntity::class,
        GithubUserHeader::class
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