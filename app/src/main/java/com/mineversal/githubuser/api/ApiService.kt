package com.mineversal.githubuser.api

import com.mineversal.githubuser.BuildConfig
import com.mineversal.githubuser.data.model.SearchResponse
import com.mineversal.githubuser.data.model.UserDetailResponse
import com.mineversal.githubuser.data.model.Users
import retrofit2.Call
import retrofit2.http.*

//API SERVICE
interface ApiService {
    @GET("search/users")
    @Headers(mySuperScretKey)
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers(mySuperScretKey)
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers(mySuperScretKey)
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Users>>

    @GET("users/{username}/following")
    @Headers(mySuperScretKey)
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Users>>

    companion object {
        private const val mySuperScretKey = BuildConfig.KEY
    }
}