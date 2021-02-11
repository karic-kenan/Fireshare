/*
 * Created by Karic Kenan on 9.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.ui.profile.adapter.ProfilePostAdapter
import io.aethibo.fireshare.ui.profile.viewmodel.ProfileViewModel
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseProfilePostFragment(layoutId: Int) : Fragment(layoutId) {

    protected val profilePostAdapter: ProfilePostAdapter by lazy { ProfilePostAdapter() }
    protected lateinit var navigator: BottomNavigator
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = BottomNavigator.provide(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePostAdapter.setOnLikeClickListener { post, position ->
            println("Liked post: ${post.caption} at position: $position")
        }

        profilePostAdapter.setOnMenuClickListener { post, position ->
            println("Menu clicked on post: ${post.caption} at position: $position")
            viewModel.singlePostOptionsMenuClicked(requireContext(), layoutInflater, post, navigator)
        }
    }
}