package com.mineversal.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListUserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    //Declaration of Click Call Back
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    //Get Id from Activity View
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var tvName: TextView = itemView.findViewById(R.id.name)
        var tvUsername: TextView = itemView.findViewById(R.id.username)
        var imgAvatar: ImageView = itemView.findViewById(R.id.avatar)
    }

    //Set Row Activity
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(view)
    }

    //Set User Data to Activity
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(55, 55))
            .into(holder.imgAvatar)
        holder.tvName.text = user.name
        holder.tvUsername.text = user.username
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    //List Count
    override fun getItemCount() = listUser.size

    //Return User Data if Clicked
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}