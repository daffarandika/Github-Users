package com.example.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.model.FavoriteUser

@Database(
    entities = [FavoriteUser::class],
    version = 1
)
abstract class FavoriteUserDatabase: RoomDatabase() {
    abstract fun favUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserDatabase? = null

        fun getInstance(context: Context): FavoriteUserDatabase =
            INSTANCE ?: synchronized(context){
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    FavoriteUserDatabase::class.java,
                    "favoriteUserDB"
                ).build()
            }
    }

}