package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ItemGithubUserLinearBinding
import com.example.githubuser.model.FavoriteUser

class FavoriteUserAdapter: ListAdapter<FavoriteUser, FavoriteUserAdapter.FViewHolder>(DIFF_UTIL) {
    companion object {
        val DIFF_UTIL: DiffUtil.ItemCallback<FavoriteUser> =
        object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class FViewHolder(val binding: ItemGithubUserLinearBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(favoriteUser: FavoriteUser) {
            binding.tvUsername.text = favoriteUser.login
            Glide.with(itemView.context)
                .load(favoriteUser.avatarUrl)
                .into(binding.ivUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FViewHolder {
        val binding = ItemGithubUserLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FViewHolder, position: Int) {
        val favoriteUser = getItem(position)
        holder.bind(favoriteUser)
    }
}