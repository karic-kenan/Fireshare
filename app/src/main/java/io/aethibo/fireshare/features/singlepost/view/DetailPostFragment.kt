package io.aethibo.fireshare.features.singlepost.view

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import io.aethibo.fireshare.R
import io.aethibo.fireshare.features.singlepost.viewmodel.SinglePostViewModel
import io.aethibo.fireshare.features.utils.BasePostFragment
import kotlinx.android.synthetic.main.layout_item_post.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailPostFragment : BasePostFragment(R.layout.layout_item_post) {

    private val args by navArgs<DetailPostFragmentArgs>()
    private val viewModel: SinglePostViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        args.singlePost.let { post ->
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
            val likeCount = post.likes.size
            postLikeCountTxt.text = when {
                likeCount <= 0 -> getString(R.string.single_post_no_likes)
                likeCount == 1 -> getString(R.string.single_post_one_like)
                else -> getString(R.string.single_post_multiple_likes, likeCount)
            }

            val commentCount = post.likes.size
            postCommentCountTxt.text = when {
                likeCount <= 0 -> getString(R.string.single_post_no_comments)
                likeCount == 1 -> getString(R.string.single_post_one_comment)
                else -> getString(R.string.single_post_multiple_comments, commentCount)
            }

            postMenu.setOnClickListener {
                viewModel.singlePostOptionsMenuClicked(requireContext(), layoutInflater, post)
            }

            postDivider.isVisible = false
        }
    }
}