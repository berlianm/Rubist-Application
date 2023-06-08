package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName

data class AddPostResponse(
    @field:SerializedName("postId")
    val postId: String? = null,

    @field:SerializedName("error")
    val error: String? = null,
)
