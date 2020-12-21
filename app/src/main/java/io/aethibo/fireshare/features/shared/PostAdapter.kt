package io.aethibo.fireshare.features.shared

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.firebase.auth.FirebaseAuth
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.Post
import kotlinx.android.synthetic.main.layout_item_post.view.*

class PostAdapter : PagingDataAdapter<Post, PostAdapter.PostViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPostImage: ImageView = itemView.postImage
        val ivAuthorProfileImage: ImageView = itemView.postAvatar
        val tvPostAuthor: TextView = itemView.postUsername
        val tvPostText: TextView = itemView.postDescription
        val tvDate: TextView = itemView.postDate
        val tvLikedBy: TextView = itemView.postLikeCountTxt
        val tvCommentedBy: TextView = itemView.postCommentCount
        val ibLike: ImageButton = itemView.postLikeButton
        val ibComment: ImageButton = itemView.postCommentButton
        val ibShare: ImageButton = itemView.postShareButton
        val ibMenu: ImageButton = itemView.postMenu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
            PostViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.layout_item_post, parent, false)
            )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: return

        holder.apply {

            ivPostImage.load(post.imageUrl) {
                crossfade(true)
                transformations(RoundedCornersTransformation(30.0f))
            }
            ivAuthorProfileImage.load(post.authorProfilePictureUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }

            tvPostAuthor.text = post.authorUsername
            tvPostText.text = post.caption
            tvDate.text = DateUtils.getRelativeTimeSpanString(post.timestamp)

            val likeCount = post.likes.size
            tvLikedBy.text = when {
                likeCount <= 0 -> "No likes"
                likeCount == 1 -> "1 like"
                else -> "$likeCount likes"
            }

            val commentCount = post.likes.size
            tvCommentedBy.text = when {
                likeCount <= 0 -> "No comments"
                likeCount == 1 -> "1 comment"
                else -> "$commentCount comments"
            }

            val uid = FirebaseAuth.getInstance().uid!!

        }
    }
}