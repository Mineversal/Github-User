package com.mineversal.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mineversal.githubuser.databinding.ItemRowUserBinding
import com.mineversal.githubuser.model.Users

class UserAdapter(private val users: List<Users>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    //Declaration of Click Call Back
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    //Set Row Activity
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
    )

    //Get Id from row Activity View
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvUsername.text = users[position].id.toString()
        viewHolder.tvName.text = users[position].login
        Glide.with(viewHolder.itemView.context)
            .load(users[position].avatar_url)
            .apply(RequestOptions().override(55, 55))
            .into(viewHolder.imgAvatar)
        viewHolder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(users[position]) }
    }

    //List Count
    override fun getItemCount() = users.size

    class ViewHolder(binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvUsername: TextView = binding.username
        val tvName: TextView = binding.name
        val imgAvatar: ImageView = binding.avatar
    }

    //Return User Data if Clicked
    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
    }
}