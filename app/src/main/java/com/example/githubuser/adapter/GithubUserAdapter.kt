package com.example.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.databinding.ItemGithubUserBinding
import com.example.githubuser.model.local.GithubUserHeader

class GithubUserAdapter(
    private val onHeartClick: (GithubUserHeader) -> Unit,
    private val onItemClick: (GithubUserHeader) -> Unit
): ListAdapter<GithubUserHeader, GithubUserAdapter.GViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GithubUserHeader> =
            object: DiffUtil.ItemCallback<GithubUserHeader>() {
                override fun areItemsTheSame(
                    oldItem: GithubUserHeader,
                    newItem: GithubUserHeader,
                ): Boolean {
                    return oldItem.login == newItem.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: GithubUserHeader,
                    newItem: GithubUserHeader,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    inner class GViewHolder(val binding: ItemGithubUserBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(user: GithubUserHeader) {
            binding.tvUsername.text = user.login
            binding.constraint.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.item_background))
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(binding.ivUser)
            itemView.setOnClickListener{
                onItemClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GViewHolder {
        val binding = ItemGithubUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return GViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        val ibHeart = holder.binding.ibHeart
        if (user.isFavorite) {
            ibHeart.setImageDrawable(ContextCompat.getDrawable(ibHeart.context, R.drawable.ic_favorite))
        } else {
            ibHeart.setImageDrawable(ContextCompat.getDrawable(ibHeart.context, R.drawable.ic_favorite_border))
        }
        ibHeart.setOnClickListener {
            onHeartClick(user)
            notifyDataSetChanged()
        }
    }
}