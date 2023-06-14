package com.C23PS480.Rubist.DetailPost

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.C23PS480.Rubist.Adapter.CommentAdapter
import com.C23PS480.Rubist.Fragment.AddPost
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModel.DetailPostViewModel
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.ActivityDetailPostBinding
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
@Suppress("DEPRECATION")
class DetailPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPostBinding
    private lateinit var userPreference: UserPreference
    private lateinit var progressBar: ProgressBar
    private lateinit var detailPostViewModel: DetailPostViewModel
    private lateinit var mainViewModel : MainViewModel
    private lateinit var commentAdapter: CommentAdapter

    companion object {
        const val KEY = "EXTRA_POST"

        var userId: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        val post = intent.getStringExtra(KEY)

        detailPostViewModel = ViewModelProvider(this)[DetailPostViewModel::class.java]

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(this.dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            userId = user.uid
        }

        post?.let{
            setupDetailPost()

        }
        commentAdapter = CommentAdapter(emptyList())
        binding.recyclerView.adapter = commentAdapter

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        observePost()
        observeLoading()
        observeComments()

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            val comment = binding.commentForm.text.toString().trim()
            if (comment.isNotEmpty()) {
                val postId = intent.getStringExtra(KEY)
                if (postId != null) {
                    detailPostViewModel.addComment(postId, comment, userId!!)
                    finish();
                    startActivity(getIntent());
                    binding.commentForm.text.clear()
                }
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDetailPost(){
        val postId = intent.getStringExtra(KEY)
        Log.d("KEY", postId.toString())
        if(postId != null){
            detailPostViewModel.getPost(postId)
            detailPostViewModel.getComments(postId)
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
                .load(post.ProfileUrl)
                .circleCrop()
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

    private fun observeComments() {
        detailPostViewModel.comments.observe(this) { comments ->
            commentAdapter.comments = comments
            commentAdapter.notifyDataSetChanged()
        }
    }


}