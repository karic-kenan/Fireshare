/*
 * Created by Karic Kenan on 17.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.timeline.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import io.aethibo.fireshare.ui.utils.formatLargeNumber
import io.aethibo.fireshare.ui.utils.startBounceAnimation

class TimelineAdapter : ListAdapter<Post, TimelineAdapter.TimelineViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder =
            TimelineViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.layout_item_post, parent, false)
            )

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: Post) = with(itemView) {

            // Init views
            val avatar = itemView.findViewById<ImageView>(R.id.postAvatar)
            val username = itemView.findViewById<TextView>(R.id.postUsername)
            val date = itemView.findViewById<TextView>(R.id.postDate)
            val image = itemView.findViewById<ImageView>(R.id.postImage)
            val caption = itemView.findViewById<TextView>(R.id.postDescription)
            val likeButton = itemView.findViewById<ImageButton>(R.id.postLikeButton)
            val likeCount = itemView.findViewById<TextView>(R.id.postLikeCountTxt)
            val commentButton = itemView.findViewById<ImageButton>(R.id.postCommentButton)

            // Init their values
            avatar.load(post.authorProfilePictureUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.default_user_image)
                error(R.drawable.default_user_image)
            }

            image.load(post.imageUrl) {
                crossfade(true)
                transformations(RoundedCornersTransformation(10f))
            }

            val postTimestamp = DateUtils.getRelativeTimeSpanString(post.timestamp)

            val formattedDate = when {
                postTimestamp.contains("0 minutes ago") -> "A moment ago"
                DateUtils.isToday(post.timestamp) -> "Today"
                else -> postTimestamp
            }

            username.text = post.authorUsername
            date.text = formattedDate
            caption.text = post.caption

            val likesSze = post.likedBy.size
            likeCount.text = formatLargeNumber(likesSze)

            likeButton.setImageResource(if (post.isLiked) R.drawable.ic_like else R.drawable.ic_unlike)

            /**
             * Click listeners
             */
            likeButton.setOnClickListener {
                onLikeClickListener?.let { click ->
                    if (!post.isLiking) {
                        click(post, absoluteAdapterPosition)
                        it.startBounceAnimation()
                    }
                }
            }

            avatar.setOnClickListener {
                onUserClickListener?.let { click -> click(post.ownerId) }
            }

            commentButton.setOnClickListener {
                onCommentClickListener?.let { click -> click(post.id, post.ownerId, post.imageUrl) }
            }
        }
    }

    /**
     * Click listeners
     */
    private var onLikeClickListener: ((Post, Int) -> Unit)? = null
    private var onUserClickListener: ((String) -> Unit)? = null
    private var onCommentClickListener: ((String, String, String) -> Unit)? = null

    fun setOnLikeClickListener(listener: (Post, Int) -> Unit) {
        onLikeClickListener = listener
    }

    fun setOnUserClickListener(listener: (String) -> Unit) {
        onUserClickListener = listener
    }

    fun setOnCommentClickListener(listener: (String, String, String) -> Unit) {
        onCommentClickListener = listener
    }
}