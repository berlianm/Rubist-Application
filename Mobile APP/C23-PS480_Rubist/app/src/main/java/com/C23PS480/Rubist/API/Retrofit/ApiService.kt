package com.C23PS480.Rubist.API.Retrofit

import com.C23PS480.Rubist.API.Request.UserRequest
import com.C23PS480.Rubist.API.Response.ChangePassResponse
import com.C23PS480.Rubist.API.Response.FileUploadResponse
import com.C23PS480.Rubist.API.Response.LoginResponse
import com.C23PS480.Rubist.API.Response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Headers("content-type: application/json")
    @POST("/api/login/")
    fun login(
        @Body userRequest: UserRequest
    ): Call<LoginResponse>

    @Headers("content-type: application/json")
    @POST("/api/register/")
    fun register(
        @Body userRequest: UserRequest
    ): Call<RegisterResponse>


    @Headers("content-type: application/json")
    @POST("/api/forget-password/")
    fun changePassword(
        @Body userRequest: UserRequest
    ): Call<ChangePassResponse>

    @Multipart
    @POST("stories/guest")
    fun uploadImage(
//        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

}