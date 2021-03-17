/*
 * Created by Karic Kenan on 17.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.timeline.adapter

import android.text.format.DateUtils
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
import coil.transform.RoundedCornersTransformation
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.Post

class TimelineAdapter : ListAdapter<Post, TimelineAdapter.TimelineViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: Post) = with(itemView) {

            // Init views
            val avatar = itemView.findViewById<ImageView>(R.id.postAvatar)
            val username = itemView.findViewById<TextView>(R.id.postUsername)
            val date = itemView.findViewById<TextView>(R.id.postDate)
            val image = itemView.findViewById<ImageView>(R.id.postImage)
            val caption = itemView.findViewById<TextView>(R.id.postDescription)

            // Init their values
            avatar.load(post.authorProfilePictureUrl) {
                transformations(CircleCropTransformation())
            }
            image.load(post.imageUrl) {
                transformations(RoundedCornersTransformation(10f))
            }

            username.text = post.authorUsername
            date.text = DateUtils.getRelativeTimeSpanString(post.timestamp)
            caption.text = post.caption
        }
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder =
            TimelineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_post, parent, false))
}