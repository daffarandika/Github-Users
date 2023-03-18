package com.example.githubuser.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.activity.DetailActivity
import com.example.githubuser.R
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.database.FavoriteUserRepository
import com.example.githubuser.model.GithubUser
import kotlinx.coroutines.runBlocking

class GithubUserAdapter(
): androidx.recyclerview.widget.ListAdapter< List<FavoriteUser>, GithubUserAdapter.GViewHolder >(DIFF_CALLBACK) {

    companion object {
        object DIFF_CALLBACK: DiffUtil.ItemCallback<List<FavoriteUser>>() {
            override fun areItemsTheSame(
                oldItem: List<FavoriteUser>,
                newItem: List<FavoriteUser>,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: List<FavoriteUser>,
                newItem: List<FavoriteUser>,
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
    inner class GViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ivAvatar: ImageView = view.findViewById(R.id.ivUser)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val ibHeart: ImageButton = view.findViewById(R.id.ibHeart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GViewHolder {
        val binding =
    }

    override fun onBindViewHolder(holder: GViewHolder, position: Int) {
        val user: GithubUser = githubUsers[position]
        val repo = FavoriteUserRepository((context as Activity).application)
        holder.tvUsername.text = user.login

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("username", user.login)
            context.startActivity(intent)
        }
        holder.ibHeart.setOnClickListener {
            val drawable = BitmapFactory.decodeResource(context.resources, R.drawable.ic_favorite)
            holder.ibHeart.setImageBitmap(drawable)
//            addFavoriteUser(FavoriteUser(
//                login = user.login,
//                avatarUrl = user.avatarUrl))
        }
        Glide.with(context)
            .load(user.avatarUrl)
            .into(holder.ivAvatar)
    }

    private fun addFavoriteUser(favoriteUser: FavoriteUser) = runBlocking{
        val repo = FavoriteUserRepository((context as Activity).application)
        repo.addNewFavoriteUser(favoriteUser)
    }
}