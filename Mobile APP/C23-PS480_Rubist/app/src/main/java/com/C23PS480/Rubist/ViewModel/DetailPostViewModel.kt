package com.C23PS480.Rubist.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.C23PS480.Rubist.API.Response.DetailPostResponse
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
}