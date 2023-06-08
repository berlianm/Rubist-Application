package com.C23PS480.Rubist.API.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GetPostResponse(
    @field:SerializedName("sample-post-id")
    val listPost: List<ListPost>,

    @field:SerializedName("error")
    val error: String
)

@Parcelize
data class ListPost(
    val title: String,
    val content: String,
    val photoUrl: String,
    val userId: String
) : Parcelable
