package com.cp23ps480.rubist.API.Response

import com.google.gson.annotations.SerializedName

data class ChangePassResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("error")
    val error: String? = null,
)
