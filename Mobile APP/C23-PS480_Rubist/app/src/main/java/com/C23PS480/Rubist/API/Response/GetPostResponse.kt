package com.C23PS480.Rubist.API.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GetPostResponse(
    val listPost: List<ListPost>,

    @field:SerializedName("error")
    val error: String
)

@Parcelize
data class ListPost(
    val content: String,
    val photoUrl: String,
    val postId: String,
    val title: String,
    val userId: String
) : Parcelable
