package com.C23PS480.Rubist.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.API.Response.DataUserResponse
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.EditProfile.EditProfileActivity
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class Profile : Fragment(), View.OnClickListener {

    private lateinit var mainViewModel : MainViewModel
    private var _binding:FragmentProfileBinding? = null;
    private val binding get() = _binding!!;

    private var uid : String? = null
    private var name : String? = null
    private var email : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(requireActivity()) { user ->
            uid = user.uid
            name = user.name
            email = user.email
            getDataUser()
        }

        val btnEdit : Button = view.findViewById(R.id.btn_EditProfile)
        btnEdit.setOnClickListener(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view;
    }

    private fun getDataUser(){
        setLoading(true)
        val getData = ApiConfig.getApiService().getDatalUser(uid!!)
        getData.enqueue(object : Callback<DataUserResponse>{
            override fun onResponse(
                call: Call<DataUserResponse>,
                response: Response<DataUserResponse>
            ) {
                if(response.isSuccessful){
                    setLoading(false)
                    val responseBody = response.body()
//                    Toast.makeText(requireContext(), "Data $uid", Toast.LENGTH_SHORT).show()
                    binding.apply {
                        EditProfileActivity.uid = responseBody?.uid
                        EditProfileActivity.name = responseBody?.name
                        EditProfileActivity.email = responseBody?.email
                        EditProfileActivity.photoUrl = responseBody?.photoUrl

                        tvProfileName.text = responseBody?.name
                        tvProfileEmail.text = responseBody?.email
                        tvProfileNumber.text = responseBody?.mobilePhone
                        tvProfileLocation.text = responseBody?.location
                        val profilePhoto= responseBody?.photoUrl
                        if (profilePhoto != null){
                            Glide.with(requireContext())
                                .load(profilePhoto)
                                .apply(RequestOptions().placeholder(R.drawable.avatar))
                                .circleCrop()
                                .into(userAvatar)
                        }

                        Log.d("Avatar", "Profile Photo: $profilePhoto")
                    }

                }else{
                    setLoading(false)
                    binding.apply {
                        EditProfileActivity.uid = uid
                        EditProfileActivity.name = name
                        EditProfileActivity.email = email

                        tvProfileEmail.text = email
                        tvProfileName.text = name

                        Glide.with(requireContext())
                            .load(R.drawable.avatar)
                            .apply(RequestOptions().placeholder(R.drawable.avatar))
                            .circleCrop()
                            .into(userAvatar)
                    }
                    Toast.makeText(requireContext(), "Get data failed", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<DataUserResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun setLoading(isLoading : Boolean){
        if (isLoading){
            binding.loading.visibility = View.VISIBLE
            binding.loading.elevation = 10f
        }else{
            binding.loading.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
    }


}