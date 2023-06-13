package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName

data class DetailPostResponse(
    @field:SerializedName("postData")
    val postData: ListPost
)
