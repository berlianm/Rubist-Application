package com.cp23ps480.rubist.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cp23ps480.rubist.AddPost.AddPostActivity
import com.cp23ps480.rubist.MainViewModel
import com.cp23ps480.rubist.NavBar.NavBarActivity
import com.cp23ps480.rubist.R
import com.cp23ps480.rubist.RecycleViewAdapter


class Community : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>? = null
    private lateinit var mainViewModel : MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val BtnAddPost: TextView = view.findViewById(R.id.tv_addPost)
       BtnAddPost.setOnClickListener {
            // Pindah ke activity lain
            val intent = Intent(activity, AddPostActivity::class.java)
            startActivity(intent)

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false)
    }




}