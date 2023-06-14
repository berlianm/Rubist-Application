package com.C23PS480.Rubist.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.C23PS480.Rubist.API.Response.AddCommentResponse
import com.C23PS480.Rubist.API.Response.CommentPostResponse
import com.C23PS480.Rubist.API.Response.DetailPostResponse
import com.C23PS480.Rubist.API.Response.ListComment
import com.C23PS480.Rubist.API.Response.ListPost
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPostViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _post = MutableLiveData<ListPost>()
    val post: LiveData<ListPost> = _post

    private val _comments = MutableLiveData<List<ListComment>>()
    val comments: LiveData<List<ListComment>> = _comments

    fun getPost(postId:String){
        _isLoading.value = true

        val apiService = ApiConfig.getApiService()
        val call = apiService.getPostbyID(postId)

        call.enqueue(object : Callback<DetailPostResponse>{
            override fun onResponse(
                call: Call<DetailPostResponse>,
                response: Response<DetailPostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val postResponse = response.body()
                    if (postResponse != null){
                        _post.value = postResponse.postData
                    }else{
                    }
                } else{

                }
            }

            override fun onFailure(call: Call<DetailPostResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Network Failure. Please Check your internet connection", t )
            }
        })
    }

    fun getComments(postId:String){
        val apiService = ApiConfig.getApiService()
        val call = apiService.getCommentbyId(postId)

        call.enqueue(object : Callback<CommentPostResponse>{
            override fun onResponse(
                call: Call<CommentPostResponse>,
                response: Response<CommentPostResponse>
            ) {
                if(response.isSuccessful){
                    val commentResponse = response.body()
                    if (commentResponse != null){
                        _comments.value = commentResponse.commentData
                    } else{
                        _comments.value = emptyList()
                    }
                } else{
                    _comments.value = emptyList()
                }
            }

            override fun onFailure(call: Call<CommentPostResponse>, t: Throwable) {
                _comments.value = emptyList()
                Log.e(TAG, "Network Failure. Please Check your internet connection", t)
            }
        })
    }

    fun addComment(postId: String, comment: String){
        Log.d("postId", postId)
        Log.d("comment", comment)
        val apiService = ApiConfig.getApiService()
        val call = apiService.addComment(postId, comment)

        call.enqueue(object : Callback<AddCommentResponse>{
            override fun onResponse(
                call: Call<AddCommentResponse>,
                response: Response<AddCommentResponse>
            ) {
                if(response.isSuccessful){
                    val commentResponse = response.body()
                    if (commentResponse !=null){
                        Log.d(TAG, "Comment Success")
                    } else{
                    }
                } else{

                }
            }

            override fun onFailure(call: Call<AddCommentResponse>, t: Throwable) {
                Log.e(TAG, "Network Failure. Please Check your internet connection", t)
            }
        })
    }
}