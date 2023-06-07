package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("displayName")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("emailVerified")
    val emailVerified: Boolean,

    @field:SerializedName("stsTokenManager")
    val token: stsTokenManager
)



data class stsTokenManager(

    @field:SerializedName("accessToken")
    val accessToken: String

)