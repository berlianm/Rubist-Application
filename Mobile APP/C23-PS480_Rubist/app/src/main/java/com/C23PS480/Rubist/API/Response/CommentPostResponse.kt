package com.C23PS480.Rubist.API.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CommentPostResponse(
    @field:SerializedName("commentData")
    val commentData: List<ListComment>
)


@Parcelize
data class ListComment(
    val commentId : String,
    val content: String,
    val createdAt:String,
    val userId : String,
    val displayName : String,
    val photoUrl : String
) : Parcelable