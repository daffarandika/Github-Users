package com.example.githubuser.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.activity.DetailActivity
import com.example.githubuser.R
import com.example.githubuser.databinding.ItemGithubUserBinding
import com.example.githubuser.databinding.ItemGithubUserFollowBinding
import com.example.githubuser.model.local.GithubUserHeader

class GithubFollowAdapter(
    private val users: List<GithubUserHeader>
): RecyclerView.Adapter<GithubFollowAdapter.GViewHolder>() {

    inner class GViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val ivUser = view.findViewById<ImageView>(R.id.ivUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_github_user_follow,
            parent,
            false)
        return GViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: GViewHolder, position: Int) {
        val user = users[position]
        holder.tvUsername.text = user.login
        Glide.with(holder.itemView)
            .load(user.avatarUrl)
            .into(holder.ivUser)
    }
}