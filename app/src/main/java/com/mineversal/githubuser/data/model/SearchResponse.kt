package com.mineversal.githubuser.data.model

import com.google.gson.annotations.SerializedName

//MODEL SEARCH USER
data class SearchResponse(
    @SerializedName("items")
    val items: ArrayList<Users>
)