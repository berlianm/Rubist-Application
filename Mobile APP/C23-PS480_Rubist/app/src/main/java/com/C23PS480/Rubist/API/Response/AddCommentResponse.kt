package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName

data class AddCommentResponse(
    @field:SerializedName("commentId")
    val commentId : String? = null,

    @field:SerializedName("error")
    val error: String? = null,
)
