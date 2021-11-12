package com.mineversal.githubuser.data.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mineversal.githubuser.api.ApiConfig
import com.mineversal.githubuser.data.database.FavoriteUser
import com.mineversal.githubuser.data.helper.FavoriteUserRepository
import com.mineversal.githubuser.data.model.SearchResponse
import com.mineversal.githubuser.data.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val _listUsers = MutableLiveData<ArrayList<Users>>()
    val listUsers: LiveData<ArrayList<Users>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getSearchUser()
    }

    fun setSearchUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUsers(query)
        client.enqueue(object: Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(null, error, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getSearchUser(): LiveData<ArrayList<Users>>{
        return _listUsers
    }

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    fun getAllNotes(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllNotes()

    companion object {
        private const val TAG = "SearchViewModel"
        private const val error = "Data Gagal Dimuat"
    }
}