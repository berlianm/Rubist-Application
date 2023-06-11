package com.C23PS480.Rubist.API.Retrofit

import com.C23PS480.Rubist.API.Request.UserRequest
import com.C23PS480.Rubist.API.Response.AddPostResponse
import com.C23PS480.Rubist.API.Response.ChangePassResponse
import com.C23PS480.Rubist.API.Response.DataUserResponse
import com.C23PS480.Rubist.API.Response.DetailPostResponse
import com.C23PS480.Rubist.API.Response.FileUploadResponse
import com.C23PS480.Rubist.API.Response.GetPostResponse
import com.C23PS480.Rubist.API.Response.LoginResponse
import com.C23PS480.Rubist.API.Response.RegisterResponse
import com.C23PS480.Rubist.API.Response.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Headers("content-type: application/json")
    @POST("/api/login/")
    fun login(
        @Body userRequest: UserRequest
    ): Call<LoginResponse>

    //@Headers("content-type: application/json")
    @POST("/api/register/")
    fun register(
        @Body userRequest: UserRequest
    ): Call<RegisterResponse>


    //@Headers("content-type: application/json")
    @POST("/api/forget-password/")
    fun changePassword(
        @Body userRequest: UserRequest
    ): Call<ChangePassResponse>

//    @Headers("content-type: application/json")
    @Multipart
    @POST("/api/updateProfile")
    fun updateProfile(
        @Part photo: MultipartBody.Part,
        @Part("location") location: RequestBody,
        @Part("mobilePhone") mobilePhone: RequestBody,
    ): Call<UpdateProfileResponse>


    //@Headers("content-type: application/json")
    @Multipart
    @POST("/uploadImage")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<FileUploadResponse>



    @Multipart
    @POST("/api/community/posts")
    fun addPost(
//        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<AddPostResponse>

    @GET("/api/community/posts")
    fun getPost(
    ): Call<GetPostResponse>

    @GET("/api/community/posts/{postId}")
    fun getPostbyID(
        @Path("postId") postId: String
    ): Call<DetailPostResponse>


    @GET("/api/getData/{uid}")
    fun getDatalUser(
        @Path("uid") uid: String
    ): Call<DataUserResponse>

}