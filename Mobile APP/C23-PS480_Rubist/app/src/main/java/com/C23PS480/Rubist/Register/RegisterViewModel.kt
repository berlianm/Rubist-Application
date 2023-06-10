package com.C23PS480.Rubist.Register

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.API.Request.UserRequest
import com.C23PS480.Rubist.Model.UserModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.API.Response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (private val pref: UserPreference) : ViewModel(){

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _Loading = MutableLiveData<Boolean>()
    val Loading : LiveData<Boolean> = _Loading

    fun register(name: String, email: String, password: String) {
        _Loading.value = true
        val client = ApiConfig.getApiService().register(UserRequest(name, email, password ))
        client.enqueue(object : Callback<RegisterResponse> {

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _Loading.value = false
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        viewModelScope.launch {
                            pref.saveUser(
                                UserModel(
                                    "",
                                    name,
                                    email,
                                    "",
                                    "",
                                    false,
                                    ""
                                )
                            )
                        }
                        _isError.value = false

                    } else {
                        _isError.value = true
                    }
                } else {
                    _Loading.value = false
                    val responseBody = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        RegisterResponse::class.java
                    )
                    _msg.value = responseBody.error.toString()
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _Loading.value = false
                _msg.value = t.message.toString()
            }
        })
    }
}