package com.cp23ps480.rubist.API.Response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("error")
    val error: String? = null,

)

