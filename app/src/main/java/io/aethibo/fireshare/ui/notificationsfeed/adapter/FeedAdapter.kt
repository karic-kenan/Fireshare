/*
 * Created by Karic Kenan on 23.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.notificationsfeed.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.ActivityFeedItem
import io.aethibo.fireshare.domain.FeedType

class FeedAdapter : ListAdapter<ActivityFeedItem, FeedAdapter.FeedViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<ActivityFeedItem>() {

        override fun areItemsTheSame(
            oldItem: ActivityFeedItem,
            newItem: ActivityFeedItem
        ): Boolean =
            oldItem.userId == newItem.userId

        override fun areContentsTheSame(
            oldItem: ActivityFeedItem,
            newItem: ActivityFeedItem
        ): Boolean =
            oldItem.hashCode() == newItem.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder =
        FeedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        )

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(feedItem: ActivityFeedItem) = with(itemView) {

            // init views
            val avatar = itemView.findViewById<ImageView>(R.id.feedItemAvatar)
            val title = itemView.findViewById<TextView>(R.id.feedItemTitle)
            val subtitle = itemView.findViewById<TextView>(R.id.feedItemSubtitle)
            val image = itemView.findViewById<ImageView>(R.id.feedItemImage)

            avatar.load(feedItem.avatar) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.default_user_image)
                error(R.drawable.default_user_image)
            }

            val notificationTimestamp = DateUtils.getRelativeTimeSpanString(feedItem.timestamp)

            val formattedDate = when {
                notificationTimestamp.contains("0 minutes ago") -> "A moment ago"
                DateUtils.isToday(feedItem.timestamp) -> "Today"
                else -> notificationTimestamp
            }

            subtitle.text = formattedDate
            image.isVisible = feedItem.imageUrl.isNotEmpty()

            when (feedItem.type) {
                FeedType.LIKE.name -> {
                    title.text = HtmlCompat.fromHtml(
                        itemView.context.getString(
                            R.string.labelFeedItemTitle,
                            feedItem.username,
                            itemView.context.getString(R.string.labelFeedItemLikePlaceholder)
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    image.load(feedItem.imageUrl) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(15f))
                    }
                }
                FeedType.FOLLOW.name -> {
                    title.text = HtmlCompat.fromHtml(
                        itemView.context.getString(
                            R.string.labelFeedItemTitle,
                            feedItem.username,
                            itemView.context.getString(R.string.labelFeedItemFollowPlaceholder)
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
                FeedType.COMMENT.name -> {
                    title.text = HtmlCompat.fromHtml(
                        itemView.context.getString(
                            R.string.labelFeedItemTitle,
                            feedItem.username,
                            itemView.context.getString(
                                R.string.labelFeedItemCommentPlaceholder,
                                feedItem.comment
                            )
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    image.load(feedItem.imageUrl) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(15f))
                    }
                }
                else -> {
                }
            }
        }
    }
}