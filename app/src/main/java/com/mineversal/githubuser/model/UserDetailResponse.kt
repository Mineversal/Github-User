package com.mineversal.githubuser.model

//MODEL API DETAIL USER
data class UserDetailResponse(
    val login: String,
    val name: String,
    val id: Int,
    val avatar_url: String,
    val follower_url: String,
    val following_url: String,
    val following: Int,
    val followers: Int,
    val html_url: String,
    val company: String,
    val location: String,
    val public_repos: Int
)