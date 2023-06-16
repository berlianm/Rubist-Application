package com.C23PS480.Rubist.Adapter

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.C23PS480.Rubist.API.Response.ListPost
import com.C23PS480.Rubist.DetailPost.DetailPostActivity
import com.C23PS480.Rubist.R
import com.bumptech.glide.Glide
import java.util.TimeZone

class CommunityAdapter (private val posts : List<ListPost>) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val photoImageView : ImageView = view.findViewById(R.id.iv_item_photo)
        val titleTextView: TextView = view.findViewById(R.id.tv_title)
        val descTextView : TextView = view.findViewById(R.id.tv_desc)
        val photoUserView : ImageView = view.findViewById(R.id.photo_user)
        val usernameTextView : TextView = view.findViewById(R.id.tv_name)
        val dateTextView : TextView = view.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        holder.titleTextView.text = post.title
        holder.descTextView.text = post.content
        holder.usernameTextView.text = post.displayName
        holder.dateTextView.text = post.createdAt
        Glide.with(holder.photoUserView)
            .load(post.ProfileUrl)
            .circleCrop()
            .into(holder.photoUserView)
        Glide.with(holder.photoImageView)
            .load(post.photoUrl)
            .into(holder.photoImageView)

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, DetailPostActivity::class.java)
            intent.putExtra(KEY, post.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    companion object{
        const val KEY = "EXTRA_POST"
    }

}