/*
 * Created by Karic Kenan on 15.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.comments.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.Comment

class CommentsAdapter : ListAdapter<Comment, CommentsAdapter.CommentsViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Comment>() {

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder = CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
    )

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Comment) = with(itemView) {

            val avatar: ImageView = itemView.findViewById(R.id.ivCommentAvatar)
            val username: TextView = itemView.findViewById(R.id.tvCommentUsername)
            val date: TextView = itemView.findViewById(R.id.tvCommentDate)
            val commentText: TextView = itemView.findViewById(R.id.tvCommentText)
            val commentMenu: ImageButton = itemView.findViewById(R.id.ibCommentMenu)

            avatar.load(item.authorProfilePictureUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }

            username.text = item.authorUsername
            date.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
            commentText.text = item.comment

            commentMenu.isVisible = item.userId == FirebaseAuth.getInstance().uid

            /**
             * Click listeners
             */
            username.setOnClickListener {
                onUserClickListener?.let { click -> click(item) }
            }

            commentMenu.setOnClickListener {
                onCommentMenuListener?.let { click -> click(item) }
            }
        }
    }

    /**
     * Click listeners
     */
    private var onUserClickListener: ((Comment) -> Unit)? = null
    private var onCommentMenuListener: ((Comment) -> Unit)? = null

    fun setOnUserClickListener(listener: (Comment) -> Unit) {
        onUserClickListener = listener
    }

    fun setOnCommentMenuClickListener(listener: (Comment) -> Unit) {
        onCommentMenuListener = listener
    }
}