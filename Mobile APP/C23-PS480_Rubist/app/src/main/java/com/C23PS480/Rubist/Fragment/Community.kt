package com.C23PS480.Rubist.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.C23PS480.Rubist.API.Response.ListPost
//import com.C23PS480.Rubist.AddPost.AddPostActivity
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.databinding.FragmentCommunityBinding

//import com.C23PS480.Rubist.RecycleViewAdapter


class Community : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get()= _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val post: MutableList<ListPost> = mutableListOf()

    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>? = null
    private lateinit var mainViewModel : MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val BtnAddPost: TextView = view.findViewById(R.id.tv_addPost)
       BtnAddPost.setOnClickListener {
            // Pindah ke activity lain
            val intent = Intent(activity, AddPost::class.java)
            startActivity(intent)
//            val intent = Intent(activity, AddPostActivity::class.java)
//            startActivity(intent)

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

//        setupRecyclerView()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}