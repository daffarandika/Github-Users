package com.example.githubuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.model.UserItem

class ReqresAdapter(
    val users :List<UserItem>,
    val context: Context
): RecyclerView.Adapter<ReqresAdapter.RViewHolder>() {

    inner class RViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvFirstname = view.findViewById<TextView>(R.id.firstName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.useritem, parent, false)
        return RViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: RViewHolder, position: Int) {
        holder.tvFirstname.text = users[position].firstName
    }
}