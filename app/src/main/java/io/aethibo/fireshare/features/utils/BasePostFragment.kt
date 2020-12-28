package io.aethibo.fireshare.features.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.aethibo.fireshare.features.profile.view.ProfileFragmentDirections
import io.aethibo.fireshare.features.shared.PostAdapter
import io.aethibo.fireshare.features.shared.ProfilePostAdapter

abstract class BasePostFragment(layoutId: Int) : Fragment(layoutId) {

    val postsAdapter: PostAdapter by lazy { PostAdapter() }
    val profilePostAdapter: ProfilePostAdapter by lazy { ProfilePostAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePostAdapter.setOnProfilePostClickListener { post, position ->
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToDetailPostFragment(post))
        }
    }
}