package com.C23PS480.Rubist.Adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.C23PS480.Rubist.API.Response.ListComment
import com.C23PS480.Rubist.R
import com.bumptech.glide.Glide
import java.util.TimeZone

class CommentAdapter (var comments : List<ListComment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder (view : View): RecyclerView.ViewHolder(view){
        val photoUserView : ImageView = view.findViewById(R.id.rv_image_user)
        val nameTextView : TextView = view.findViewById(R.id.rv_username)
        val dateTextView : TextView = view.findViewById(R.id.rv_date)
        val commentTextView : TextView = view.findViewById(R.id.rv_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]

        holder.nameTextView.text = comment.displayName
        holder.dateTextView.text = comment.createdAt
        holder.commentTextView.text = comment.content
        Glide.with(holder.photoUserView)
            .load(comment.photoUrl)
            .circleCrop()
            .into(holder.photoUserView)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

}