package com.cp23ps480.rubist.API.Retrofit

import com.cp23ps480.rubist.API.Request.UserRequest
import com.cp23ps480.rubist.API.Response.ChangePassResponse
import com.cp23ps480.rubist.API.Response.LoginResponse
import com.cp23ps480.rubist.API.Response.RegisterResponse
import com.cp23ps480.rubist.API.Response.StoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

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

}