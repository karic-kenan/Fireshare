/*
 * Created by Karic Kenan on 6.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.discovery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.User

class UserAdapter : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: User) = with(itemView) {

            val avatar: ImageView = itemView.findViewById(R.id.ivProfileAvatar)
            val username: TextView = itemView.findViewById(R.id.tvUsername)

            avatar.load(item.photoUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.default_user_image)
                error(R.drawable.default_user_image)
            }

            username.text = item.username

            this.setOnClickListener {
                onUserClickListener?.let { click ->
                    click(item)
                }
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    /**
     * Click listeners
     */
    private var onUserClickListener: ((User) -> Unit)? = null

    fun setOnUserClickListener(listener: (User) -> Unit) {
        onUserClickListener = listener
    }
}