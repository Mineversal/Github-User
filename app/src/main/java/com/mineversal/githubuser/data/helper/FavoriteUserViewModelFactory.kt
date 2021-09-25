package com.mineversal.githubuser.data.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mineversal.githubuser.data.viewmodel.SearchViewModel
import com.mineversal.githubuser.data.viewmodel.UserDetailViewModel

class FavoriteUserViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): FavoriteUserViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavoriteUserViewModelFactory::class.java) {
                    INSTANCE = FavoriteUserViewModelFactory(application)
                }
            }
            return INSTANCE as FavoriteUserViewModelFactory
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}