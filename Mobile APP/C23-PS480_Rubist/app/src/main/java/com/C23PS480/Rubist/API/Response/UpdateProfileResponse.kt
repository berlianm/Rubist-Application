package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    )