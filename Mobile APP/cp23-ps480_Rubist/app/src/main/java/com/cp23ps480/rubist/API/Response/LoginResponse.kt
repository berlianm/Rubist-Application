package com.cp23ps480.rubist.API.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class LoginResponse(
    @field:SerializedName("uid")
    val uid: String,

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