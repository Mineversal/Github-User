package com.mineversal.githubuser.data.helper

import android.app.Application
import androidx.lifecycle.LiveData
import com.mineversal.githubuser.data.database.FavoriteUser
import com.mineversal.githubuser.data.database.FavoriteUserDao
import com.mineversal.githubuser.data.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }
    fun getAllNotes(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }
    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }
    fun checkUser(favoriteUserId: Int) = mFavoriteUserDao.checkUser(favoriteUserId)
}