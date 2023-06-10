package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName

data class DataUserResponse(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("uid")
    val uid: String? = null,

    @field:SerializedName("mobilePhone")
    val mobilePhone: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("error")
    val error: String? = null,
)
