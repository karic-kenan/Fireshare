package io.aethibo.fireshare.features.singlepost.view

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.Post
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.core.utils.FirebaseUtil.auth
import io.aethibo.fireshare.features.comment.view.CommentsFragment
import io.aethibo.fireshare.features.singlepost.viewmodel.DetailPostViewModel
import io.aethibo.fireshare.features.utils.BasePostFragment
import io.aethibo.fireshare.features.utils.snackBar
import io.aethibo.fireshare.features.utils.startBounceAnimation
import kotlinx.android.synthetic.main.layout_item_post.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class DetailPostFragment : BasePostFragment(R.layout.layout_item_post) {

    private val viewModel: DetailPostViewModel by viewModel()

    private lateinit var post: Post

    companion object {
        fun newInstance(post: Post) = DetailPostFragment().apply {
            arguments = Bundle().apply {
                putParcelable("post", post)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postMenu?.isVisible = auth.uid == post.ownerId

        setupViews()
        subscribeToObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Post>("post")?.let { post ->
            this.post = post
        }
    }

    private fun subscribeToObservers() {
        viewModel.deletePostStatus.observe(viewLifecycleOwner, EventObserver(
                onError = { snackBar(it) },
                onSuccess = {
                    Timber.i("Post ${it.id} deleted")
                    BottomNavigator.provide(requireActivity()).pop()
                }
        ))

        viewModel.likePostStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {},
                onSuccess = {},
                onError = { error ->
                    snackBar(error)
                }
        ))
    }

    private fun setupViews() = post.let { post ->
        postAvatar.load(post.authorProfilePictureUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        postImage.load(post.imageUrl) {
            crossfade(true)
            transformations(RoundedCornersTransformation(30f))
        }
        postUsername.text = post.authorUsername
        postDate.text = DateUtils.getRelativeTimeSpanString(post.timestamp)
        postDescription.text = post.caption

        postLikeButton?.setOnClickListener {
            it.startBounceAnimation()
            viewModel.toggleLikeForPost(post)
        }

        val likeCount = post.likedBy.size
        postLikeCountTxt.text = when {
            likeCount <= 0 -> getString(R.string.single_post_no_likes)
            likeCount == 1 -> getString(R.string.single_post_one_like)
            else -> getString(R.string.single_post_multiple_likes, likeCount)
        }

        val commentCount = post.likedBy.size
        postCommentCountTxt.text = when {
            likeCount <= 0 -> getString(R.string.single_post_no_comments)
            likeCount == 1 -> getString(R.string.single_post_one_comment)
            else -> getString(R.string.single_post_multiple_comments, commentCount)
        }

        postMenu.setOnClickListener {
            viewModel.singlePostOptionsMenuClicked(
                    requireContext(),
                    layoutInflater,
                    post,
                    BottomNavigator.provide(requireActivity())
            )
        }

        postCommentButton?.setOnClickListener {
            BottomNavigator.provide(requireActivity())
                    .addFragment(CommentsFragment.newInstance(post.id))
        }

        postDivider.isVisible = false
    }
}