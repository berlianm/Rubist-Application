package com.cp23ps480.rubist.ForgetPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cp23ps480.rubist.API.Request.UserRequest
import com.cp23ps480.rubist.API.Response.ChangePassResponse
import com.cp23ps480.rubist.API.Response.LoginResponse
import com.cp23ps480.rubist.API.Retrofit.ApiConfig
import com.cp23ps480.rubist.MainActivity
import com.cp23ps480.rubist.R
import com.cp23ps480.rubist.Register.RegisterActivity
import com.cp23ps480.rubist.databinding.ActivityForgetPasswordBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener{
            val email = binding.etEmail.text.toString()
            setLoading(true)
            Send(email)
        }
    }

    fun Send(email : String){
        val Client = ApiConfig.getApiService().changePassword(UserRequest(null, email, null))

        Client.enqueue(object : Callback<ChangePassResponse> {
            override fun onResponse(
                call: Call<ChangePassResponse>,
                response: Response<ChangePassResponse>
            ) {
                if (response.isSuccessful) {
                    setLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        AlertDialog.Builder(this@ForgetPasswordActivity).apply {
                            setTitle("Yeah!")
                            setMessage(responseBody.status)
                            setPositiveButton("OK") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
                else {
                    setLoading(false)
                    val responseBody = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        LoginResponse::class.java
                    )
                    AlertDialog.Builder(this@ForgetPasswordActivity).apply {
                        setTitle("Error!")
                        setMessage(responseBody.error)
                        setPositiveButton("OK") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }

            override fun onFailure(call: Call<ChangePassResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(this@ForgetPasswordActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setLoading(isLoading : Boolean){
        if (isLoading){
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.loading.visibility = View.GONE
        }
    }
}