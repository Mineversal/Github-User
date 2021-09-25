package com.mineversal.githubuser.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//Data Class User with Parcelize
@Parcelize
data class User(
    val id: Int,
    val username: String,
    val name: String,
    val avatar: Int,
    val avatar_url: String,
    val company: String,
    val location: String,
    val repository: Int,
    val follower: Int,
    val following: Int
) : Parcelable
