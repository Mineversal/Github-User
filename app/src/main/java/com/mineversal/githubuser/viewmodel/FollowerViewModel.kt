package com.mineversal.githubuser.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mineversal.githubuser.api.ApiConfig
import com.mineversal.githubuser.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel: ViewModel() {
    private val _user = MutableLiveData<ArrayList<Users>>()
    val user: LiveData<ArrayList<Users>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getListUser()
    }

    fun setUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object: Callback<ArrayList<Users>>{
            override fun onResponse(
                call: Call<ArrayList<Users>>,
                response: Response<ArrayList<Users>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _user.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(null, error, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getListUser(): LiveData<ArrayList<Users>>{
        return _user
    }

    companion object {
        private const val TAG = "FollowerViewModel"
        private const val error = "Data Gagal Dimuat"
    }
}