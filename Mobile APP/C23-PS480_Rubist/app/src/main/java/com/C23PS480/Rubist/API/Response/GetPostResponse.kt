package com.C23PS480.Rubist.API.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GetPostResponse(

    @field:SerializedName("listPostData")
    val listPost: List<ListPost>

)

@Parcelize
data class ListPost(
    val id : String,
    val content: String,
    val createdAt:String,
    val photoUrl: String,
    val postId: String,
    val title: String,
    val userId: String,
    val displayName: String,
    val ProfileUrl:String
) : Parcelable
