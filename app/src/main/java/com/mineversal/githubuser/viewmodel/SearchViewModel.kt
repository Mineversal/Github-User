package com.mineversal.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mineversal.githubuser.api.ApiConfig
import com.mineversal.githubuser.model.SearchResponse
import com.mineversal.githubuser.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<ArrayList<Users>>()
    val listUsers: LiveData<ArrayList<Users>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "SearchViewModel"
    }

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
            }

        })
    }

    private fun getSearchUser(): LiveData<ArrayList<Users>>{
        return _listUsers
    }
}