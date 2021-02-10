/*
 * Created by Karic Kenan on 5.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.Post

class ProfilePostAdapter :
        PagingDataAdapter<Post, ProfilePostAdapter.ProfilePostViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    inner class ProfilePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPostImage: ImageView = itemView.findViewById(R.id.profilePostImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder =
            ProfilePostViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.layout_item_profile_post, parent, false)
            )

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        val post = getItem(position) ?: return

        holder.apply {

            ivPostImage.load(post.imageUrl) {
                crossfade(true)
                transformations(RoundedCornersTransformation(30.0f))
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
            }

            /**
             * Click listeners
             */
            ivPostImage.setOnClickListener {
                onProfilePostClickListener?.let { click ->
                    click(post, position)
                }
            }
        }
    }

    /**
     * Click listeners
     */
    private var onProfilePostClickListener: ((Post, Int) -> Unit)? = null

    fun setOnProfilePostClickListener(listener: (Post, Int) -> Unit) {
        onProfilePostClickListener = listener
    }
}