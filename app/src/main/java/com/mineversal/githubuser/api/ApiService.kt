package com.mineversal.githubuser.api

import com.mineversal.githubuser.model.SearchResponse
import com.mineversal.githubuser.model.UserDetailResponse
import com.mineversal.githubuser.model.Users
import retrofit2.Call
import retrofit2.http.*

//API SERVICE
interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Users>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Users>>
}