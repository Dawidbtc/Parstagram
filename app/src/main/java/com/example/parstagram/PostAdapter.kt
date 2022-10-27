package com.example.parstagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.internal.notify

class PostAdapter(val context: Context, val posts: ArrayList<Post>)
    : RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    override fun getItemCount(): Int {
        return posts.size
    }

    fun clear(){
        posts.clear()
        notifyDataSetChanged()
    }
    fun addAll(posts: ArrayList<Post>){
        posts.addAll(posts)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username: TextView
        val ivImage : ImageView
        val description : TextView

        init {
            username = itemView.findViewById(R.id.username)
            ivImage = itemView.findViewById(R.id.ivImage)
            description = itemView.findViewById(R.id.description)
        }
        fun bind(post: Post){
            description.text = post.getDescription()
            username.text = post.getUser()?.username
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)
        }
    }
}