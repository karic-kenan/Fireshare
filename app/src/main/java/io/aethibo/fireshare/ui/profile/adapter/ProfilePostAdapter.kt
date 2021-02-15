/*
 * Created by Karic Kenan on 5.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.profile.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.FirebaseUtil
import io.aethibo.fireshare.ui.utils.formatLargeNumber
import io.aethibo.fireshare.ui.utils.startBounceAnimation

class ProfilePostAdapter :
        PagingDataAdapter<Post, ProfilePostAdapter.ProfilePostViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    inner class ProfilePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.postAvatar)
        val username: TextView = itemView.findViewById(R.id.postUsername)
        val timestamp: TextView = itemView.findViewById(R.id.postDate)
        val caption: TextView = itemView.findViewById(R.id.postDescription)
        val image: ImageView = itemView.findViewById(R.id.postImage)
        val likeButton: ImageButton = itemView.findViewById(R.id.postLikeButton)
        val likeCount: TextView = itemView.findViewById(R.id.postLikeCountTxt)
        val commentButton: ImageButton = itemView.findViewById(R.id.postCommentButton)
        val commentCount: TextView = itemView.findViewById(R.id.postCommentCountTxt)
        val menu: ImageButton = itemView.findViewById(R.id.postMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder =
            ProfilePostViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_profile_post, parent, false)
            )

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        val post = getItem(position) ?: return

        holder.apply {

            menu.isVisible = FirebaseUtil.auth.uid == post.ownerId

            username.text = post.authorUsername
            timestamp.text = DateUtils.getRelativeTimeSpanString(post.timestamp)
            caption.text = post.caption

            val likesSze = post.likedBy.size
            likeCount.text = formatLargeNumber(likesSze)

            avatar.load(post.authorProfilePictureUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.mipmap.ic_launcher_round)
                error(R.mipmap.ic_launcher_round)
            }

            image.load(post.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
            }

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

            menu.setOnClickListener {
                onMenuClickListener?.let { click ->
                    click(post, absoluteAdapterPosition)
                }
            }

            commentButton.setOnClickListener {
                onCommentClickListener?.let { click -> click(post.id, absoluteAdapterPosition) }
            }
        }
    }

    /**
     * Click listeners
     */
    private var onLikeClickListener: ((Post, Int) -> Unit)? = null
    private var onCommentClickListener: ((String, Int) -> Unit)? = null
    private var onMenuClickListener: ((Post, Int) -> Unit)? = null

    fun setOnLikeClickListener(listener: (Post, Int) -> Unit) {
        onLikeClickListener = listener
    }

    fun setOnMenuClickListener(listener: (Post, Int) -> Unit) {
        onMenuClickListener = listener
    }

    fun setOnCommentClickListener(listener: (String, Int) -> Unit) {
        onCommentClickListener = listener
    }
}