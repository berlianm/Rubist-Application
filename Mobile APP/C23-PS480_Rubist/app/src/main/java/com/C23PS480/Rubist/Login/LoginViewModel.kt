package com.C23PS480.Rubist.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.API.Request.UserRequest
import com.C23PS480.Rubist.Model.UserModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.API.Response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _Loading = MutableLiveData<Boolean>()
    val Loading : LiveData<Boolean> = _Loading


    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(token: String) {
        viewModelScope.launch {
            pref.login(token)
        }
    }

    fun Auth(email: String, password: String) {
        _Loading.value = true
        val client = ApiConfig.getApiService().login(UserRequest(null, email, password))
        client.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                _msg.value = t.message.toString()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _Loading.value = false
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.emailVerified) {
                        _msg.value = "Login Success"
                        viewModelScope.launch {
                            if (getUser().value == null) pref.saveUser(
                                UserModel(
                                    responseBody.uid,
                                    //responseBody.Body.name,
                                    true,
                                    responseBody.token.accessToken
                                )
                            )
                            pref.login(responseBody.token.accessToken)
                        }
                    } else {
                        _msg.value = responseBody?.error
                    }
                } else {
                    _Loading.value = false
                    val responseBody = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        LoginResponse::class.java
                    )
                    _msg.value = responseBody.error
                }
            }
        })
    }


}