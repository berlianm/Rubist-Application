package com.cp23ps480.rubist.API.Request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)
