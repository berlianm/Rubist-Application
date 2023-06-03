//package com.C23PS480.Rubist
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.C23PS480.Rubist.API.Response.ListStory
//import com.C23PS480.Rubist.databinding.ItemPostBinding
//
//class RecycleViewAdapter(private val listStory: List<ListStory>) : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>()  {
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//
//    class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = listStory.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(holder.itemView.context)
//            .load(listStory[position].photoUrl)
//            .into(holder.binding.ivItemPhoto)
//
//        holder.binding.tvNameUser.text = listStory[position].name
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(listStory[position])
//        }
//    }
//
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//
//    interface OnItemClickCallback {
//        fun onItemClicked(listStory: ListStory)
//    }
//
//
//}