package com.C23PS480.Rubist.DetailPost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModel.DetailPostViewModel
import com.C23PS480.Rubist.databinding.ActivityDetailPostBinding
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class DetailPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPostBinding
    private lateinit var userPreference: UserPreference
    private lateinit var progressBar: ProgressBar
    private lateinit var detailPostViewModel: DetailPostViewModel

    companion object {
        const val KEY = "EXTRA_POST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        val post = intent.getStringExtra(KEY)

        detailPostViewModel = ViewModelProvider(this)[DetailPostViewModel::class.java]

        post?.let{
            setupDetailPost()
        }

        observePost()
        observeLoading()

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setupDetailPost(){
        val postId = intent.getStringExtra(KEY)
        Log.d("KEY", postId.toString())
        if(postId != null){
            detailPostViewModel.getPost(postId)
        }
    }

    private fun observePost(){
        detailPostViewModel.post.observe(this){post ->
            binding.tvUsername.text = post.displayName
            binding.tvTitle.text = post.title
            binding.tvDesc.text = post.content
            binding.tvDate.text = post.createdAt
            Glide.with(this)
                .load(post.photoUrl)
                .into(binding.tvPhoto)

            Glide.with(this)
                .load(post.photoURL)
                .into(binding.photoUser)
        }
    }

    private fun observeLoading(){
        detailPostViewModel.isLoading.observe(this){ isLoading ->
            if(isLoading){
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}