package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuser.model.Result
import com.example.githubuser.model.local.GithubUserDetailEntity
import com.example.githubuser.model.local.GithubUserHeader
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.networking.ApiService

class GithubUserRepository private constructor(
    private val apiService: ApiService,
    private val githubUserDao: GithubUserDao
) {
    fun getInitialUser(): LiveData<Result<List<GithubUserHeader>>> = liveData{
        emit(Result.Loading)
        try {
            val githubUser = apiService.getInitialUsers()
            val usersList = githubUser.map {user ->
                GithubUserHeader(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    isFavorite = githubUserDao.isUserFavorite(user.login)
                )
            }
            githubUserDao.insertUserHeaders(usersList)
        } catch (e: java.lang.Exception){
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<GithubUserHeader>>> = githubUserDao.getUserHeaders().map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    suspend fun insertUsers(users: List<GithubUserHeader>) {
        githubUserDao.insertUserHeaders(users)
    }

    fun searchUser(query: String): LiveData<Result<List<GithubUserHeader>>> = liveData {
        emit(Result.Loading)
        try {
            val users = ApiConfig.getApiService().searchUser(query).items.map { user ->
                val isUserFavorite = isUserFavorite(user.login)
                GithubUserHeader(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    isFavorite = isUserFavorite
                )
            }
            insertUsers(users)
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<GithubUserHeader>>> = githubUserDao.getUserHeaders().map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    suspend fun insertUserDetail(githubUserDetailEntity: GithubUserDetailEntity){
        githubUserDao.insertUserDetail(githubUserDetailEntity)
    }

    suspend fun isUserFavorite(login: String) = githubUserDao.isUserFavorite(login)

    suspend fun setUserAsFavorite(login: String) = githubUserDao.setUserAsFavorite(login)

    suspend fun unsetUserAsFavorite(login: String) = githubUserDao.unsetUserAsFavorite(login)

    fun getUserDetail(login: String): LiveData<Result<GithubUserDetailEntity>> = liveData{
        emit(Result.Loading)
        try {
            val localData: LiveData<Result<GithubUserDetailEntity>> = githubUserDao.getUserDetail(login).map { user ->
                Result.Success(user)
            }
            emitSource(localData)
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

//
//    suspend fun isUserFavorite(login: String) = githubUserDao.isUserFavorite(login)
//
//    suspend fun addDetailToUser(
//        githubUserEntity: GithubUserEntity,
//        name: String,
//        bio: String,
//        followers: Int,
//        following: Int,
//        isFavorite: Boolean
//    ) {
//        githubUserEntity.name = name
//        githubUserEntity.bio = bio
//        githubUserEntity.followers = followers
//        githubUserEntity.following = following
//        githubUserEntity.isFavorite = isFavorite
//        githubUserDao.updateGithubUserEntity(githubUserEntity)
//    }
//
//    //fun getFollower(login: String): LiveData<com.example.githubuser.model.Result<List<G>>>
//
//    suspend fun insertFollowing(following: List<GithubUserFollowing>){
//        githubUserDao.upsertFollowing(following)
//    }
//
//    suspend fun insertFollowers(followers: List<GithubUserFollowers>){
//        githubUserDao.upsertFollowers(followers)
//    }
//
//    fun getFavoriteUser(): LiveData<Result<List<GithubUserEntity>>> = liveData {
//        emit(Result.Loading)
//        try {
//            emitSource(githubUserDao.getAllFavoriteUser().map {
//                Result.Success(it)
//            })
//        } catch (e: Exception) {
//            emit(Result.Error(e.message.toString()))
//        }
//    }
//
//    suspend fun setUserFavorite(githubUserEntity: GithubUserEntity, isFavorite: Boolean){
//        githubUserEntity.isFavorite = isFavorite
//        githubUserDao.updateGithubUserEntity(githubUserEntity)
//    }
//
//    fun getAllUsers(): LiveData<Result<List<GithubUserEntity>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val githubUser = apiService.getInitialUsers()
//            val usersList = githubUser.map {user ->
//                GithubUserEntity(
//                    login = user.login,
//                    avatarUrl = user.avatarUrl,
//                    url = user.url,
//                    isFavorite = githubUserDao.isUserFavorite(user.login),
//                    bio = "",
//                    followers = -1,
//                    following = -1,
//                    name = ""
//                )
//            }
//            githubUserDao.upsertGithubUsers(usersList)
//        } catch (e: java.lang.Exception) {
//            emit(Result.Error(e.message.toString()))
//        }
//        val localData: LiveData<Result<List<GithubUserEntity>>> = githubUserDao.getAllUsers().map {
//            Result.Success(it)
//        }
//        emitSource(localData)
//    }
//
////    fun getFollowing(username: String):LiveData<Result<List<GithubUserFollowing>>> = liveData {
////        emit(Result.Loading)
////        try {
////            val following:List<GithubUser> = apiService.getFollowings(username)
////            githubUserDao.upsertFollowers(following.map { user ->
////                GithubUserFollowers(
////                    id = 0,
////                    githubUserLogin = username,
////                    followersLogin = user.login
////                )
////            })
////        } catch (e: Exception) {
////            emit(Result.Error(e.message.toString()))
////        }
////        val localData: LiveData<Result<List<GithubUserFollowing>>> = githubUserDao.getUserFollowing(username).map { githubUserWithFollowing ->
////            Result.Success(githubUserWithFollowing.first().following)
////        }
////        emitSource(localData)
////    }
////
////    fun getFollowers(username: String):LiveData<Result<List<GithubUserFollowers>>> = liveData {
////        emit(Result.Loading)
////        try {
////            val followers:List<GithubUser> = apiService.getFollowers(username)
////            githubUserDao.upsertFollowers(followers.map { user ->
////                GithubUserFollowers(
////                    id = 0,
////                    githubUserLogin = username,
////                    followersLogin = user.login
////                )
////            })
////        } catch (e: Exception) {
////            emit(Result.Error(e.message.toString()))
////        }
////        val localData: LiveData<Result<List<GithubUserFollowers>>> = githubUserDao.getUserFollowers(username).map { githubUserWithFollowers ->
////            Result.Success(githubUserWithFollowers.first().followers)
////        }
////        emitSource(localData)
////    }
//
    fun getFavoriteUsers(): LiveData<Result<List<GithubUserHeader>>> = liveData {
        emit(Result.Loading)
        try {
            val favoriteUsers: LiveData<Result<List<GithubUserHeader>>> = githubUserDao.getFavoriteUsers().map {
                Result.Success(it)
            }
            emitSource(favoriteUsers)
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GithubUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            githubUserDao: GithubUserDao
        ): GithubUserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: GithubUserRepository(apiService, githubUserDao)
            }.also{
                INSTANCE = it
            }
    }
}