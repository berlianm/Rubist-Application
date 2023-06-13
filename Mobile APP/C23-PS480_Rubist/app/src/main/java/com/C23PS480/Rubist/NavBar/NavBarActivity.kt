package com.C23PS480.Rubist.NavBar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.API.Response.DataUserResponse
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.EditProfile.EditProfileActivity
import com.C23PS480.Rubist.Fragment.Profile
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.ActivityNavBarBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class NavBarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNavBarBinding
    private lateinit var mainViewModel: MainViewModel

    private var uid : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            uid = user.uid
            getDataUser()
        }


        binding.signoutMenu.setOnClickListener{
            logoutDialog()
        }

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }

        binding.profileMenu.setOnClickListener{
            finish()
        }
    }
    private fun getDataUser(){
        val getData = ApiConfig.getApiService().getDatalUser(uid!!)
        getData.enqueue(object : Callback<DataUserResponse> {
            override fun onResponse(
                call: Call<DataUserResponse>,
                response: Response<DataUserResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
//                    Toast.makeText(requireContext(), "Data $uid", Toast.LENGTH_SHORT).show()
                    binding.apply {
                        binding.tvUsername.text = responseBody?.name
                        val profilePhoto= responseBody?.photoUrl
                        Glide.with(this@NavBarActivity)
                            .load(profilePhoto)
                            .apply(RequestOptions().placeholder(R.drawable.avatar))
                            .circleCrop()
                            .into(userAvatar)
                        Log.d("Avatar", "Profile Photo: $profilePhoto")
                    }

                }else{

                    Toast.makeText(this@NavBarActivity, "gagal", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<DataUserResponse>, t: Throwable) {
                Toast.makeText(this@NavBarActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }



    private fun logoutDialog() {
        val dialogMessage = getString(R.string.logout_msg)
        val dialogTitle = getString(R.string.logout)


        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)

        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                mainViewModel.logout()
                finish()
            }
            .setNegativeButton(getString(R.string.No)) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}