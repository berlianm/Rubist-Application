package com.C23PS480.Rubist.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.C23PS480.Rubist.API.Response.GetPostResponse
import com.C23PS480.Rubist.API.Response.ListPost
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.Adapter.CommunityAdapter
import com.C23PS480.Rubist.Login.LoginActivity
//import com.C23PS480.Rubist.AddPost.AddPostActivity
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModel.CommunityViewModel
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.FragmentCommunityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.C23PS480.Rubist.RecycleViewAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class Community : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get()= _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var communityAdapter : CommunityAdapter
    private lateinit var communityViewModel: CommunityViewModel

    private val post: MutableList<ListPost> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        communityViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[CommunityViewModel::class.java]

        communityViewModel.getUser().observe(this){ user ->
            if (user.isLogin){
                fetchPosts()
            } else{
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        val root :View = binding.root

        recyclerView = binding.rvPost
        progressBar = binding.progressBar

        setupRecyclerView()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        communityAdapter = CommunityAdapter(post)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = communityAdapter
    }

    private fun fetchPosts(){
        progressBar.visibility = View.VISIBLE

        val apiService = ApiConfig.getApiService()
        val call = apiService.getPost()

        call.enqueue(object : Callback<GetPostResponse>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<GetPostResponse>,
                response: Response<GetPostResponse>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val allPostResponse = response.body()
                    Log.d("AllPostResponse", allPostResponse.toString())
                    if (allPostResponse != null) {
                        val postList = allPostResponse.listPost
                        if (postList != null) {
                            post.clear()
                            post.addAll(postList)
                            communityAdapter.notifyDataSetChanged()
                        } else {
                            // Handle the case when postList is null
                            Toast.makeText(
                                requireContext(),
                                "Post list is null",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle the case when allPostResponse is null
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch post community",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Handle the case when response is not successful
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch post community",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }



            override fun onFailure(call: Call<GetPostResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to fetch post community", Toast.LENGTH_SHORT).show()
            }
        })
    }


}