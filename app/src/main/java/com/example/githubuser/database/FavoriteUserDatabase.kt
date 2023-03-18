package com.example.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteUser::class],
    version = 1
)
abstract class FavoriteUserDatabase: RoomDatabase() {
    abstract fun dao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserDatabase? = null

        fun getInstance(context: Context): FavoriteUserDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteUserDatabase::class.java,
                        "favoriteUserDB"
                    ).build()
                }
            }
            return INSTANCE as FavoriteUserDatabase
        }
    }
}