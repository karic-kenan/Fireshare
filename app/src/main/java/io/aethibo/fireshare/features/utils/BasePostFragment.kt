package io.aethibo.fireshare.features.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.features.shared.PostAdapter
import io.aethibo.fireshare.features.shared.ProfilePostAdapter
import io.aethibo.fireshare.features.singlepost.view.DetailPostFragment

abstract class BasePostFragment(layoutId: Int) : Fragment(layoutId) {

    val postsAdapter: PostAdapter by lazy { PostAdapter() }
    val profilePostAdapter: ProfilePostAdapter by lazy { ProfilePostAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePostAdapter.setOnProfilePostClickListener { post, position ->
            try {
                BottomNavigator.provide(requireActivity())
                    .addFragment(DetailPostFragment.newInstance(post))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}