package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName

data class GetPostResponse(
    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("error")
    val error: String? = null,
)
