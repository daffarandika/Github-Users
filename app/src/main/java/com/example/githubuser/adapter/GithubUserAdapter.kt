package com.example.githubuser.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.DetailActivity
import com.example.githubuser.R
import com.example.githubuser.model.GithubUser

class GithubUserAdapter(
    val layoutId: Int,
    val githubUsers: List<GithubUser>,
    val context: Context
): RecyclerView.Adapter<GithubUserAdapter.GViewHolder>() {
    inner class GViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivUser)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GViewHolder {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return GViewHolder(view)
    }

    override fun getItemCount(): Int {
        return githubUsers.size
    }

    override fun onBindViewHolder(holder: GViewHolder, position: Int) {
        val user: GithubUser = githubUsers[position]
        holder.tvUsername.text = user.login
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("username", user.login)
            (context as Activity).startActivity(intent)
        }
        Glide.with(context)
            .load(user.avatarUrl)
            .into(holder.ivAvatar)
    }
}