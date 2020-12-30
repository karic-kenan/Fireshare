package io.aethibo.fireshare.features.singlepost.misc

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.Comment

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.hashCode() == newItem.hashCode()
    }

    private val differ = AsyncListDiffer(this, diffUtil)

    var comments: List<Comment>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.ivCommentAvatar)
        val username: TextView = itemView.findViewById(R.id.tvCommentUsername)
        val date: TextView = itemView.findViewById(R.id.tvCommentDate)
        val commentText: TextView = itemView.findViewById(R.id.tvCommenText)
        val commentMenu: ImageButton = itemView.findViewById(R.id.ibCommentMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]

        holder.apply {

            avatar.load(comment.authorProfilePictureUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }

            username.text = comment.authorUsername
            date.text = DateUtils.getRelativeTimeSpanString(comment.timestamp)
            commentText.text = comment.comment

            commentMenu.isVisible = comment.userId == FirebaseAuth.getInstance().uid

            /**
             * Click listeners
             */
            username.setOnClickListener {
                onUserClickListener?.let { click ->
                    click(comment)
                }
            }

            commentMenu.setOnClickListener {
                onDeleteCommentListener?.let { click ->
                    click(comment)
                }
            }
        }
    }

    /**
     * Click listeners
     */
    private var onUserClickListener: ((Comment) -> Unit)? = null
    private var onDeleteCommentListener: ((Comment) -> Unit)? = null

    fun setOnUserClickListener(listener: (Comment) -> Unit) {
        onUserClickListener = listener
    }

    fun setOnDeleteCommentClickListener(listener: (Comment) -> Unit) {
        onDeleteCommentListener = listener
    }
}