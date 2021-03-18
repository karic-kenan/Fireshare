/*
 * Created by Karic Kenan on 7.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.othersprofile.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentProfileBinding
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.profile.view.ProfileFragment
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.android.synthetic.main.layout_profile_header.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

class OthersProfileFragment : ProfileFragment(), View.OnClickListener {

    private lateinit var userId: String
    private val binding: FragmentProfileBinding by viewBinding()

    override val uid: String
        get() = userId

    companion object {
        fun newInstance(uid: String) = OthersProfileFragment().apply {
            arguments = Bundle().apply {
                putString("uid", uid)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString("uid")?.let { uid ->
            this.userId = uid
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        viewModel.checkIsFollowing(uid)

        subscribeToObservers()

        btnProfileFollow?.setOnClickListener {
            viewModel.toggleFollowUser(uid)
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.profileMeta.collect { value: Resource<User> ->
                when (value) {
                    is Resource.Init -> Timber.d("Fetching user metadata")
                    is Resource.Loading -> Timber.d("Loading user metadata fetch")
                    is Resource.Success -> {
                        binding.profileHeader.btnProfileFollow.isVisible = true
                        val data = value.data as User
                        Timber.d("User fetched: ${data.username}")
                    }
                    is Resource.Failure -> {
                        Timber.d("Error: Failed to fetch user")
                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.followStatus.collect { handleFollowUi(it) }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.isFollowingStatus.collectLatest { handleFollowUi(it) }
        }
    }

    // TODO: Handle UI properly
    private fun handleFollowUi(value: Resource<Boolean>) {
        when (value) {
            is Resource.Init -> Timber.d("Init follow user")
            is Resource.Loading -> binding.profileProgressBar.isVisible = true
            is Resource.Success -> {
                binding.profileProgressBar.isVisible = false
                val isFollowing = value.data as Boolean

                if (isFollowing)
                    setupFollowUi()
                else
                    setupUnFollowUi()

                Timber.d("User follow is: $isFollowing")
            }
            is Resource.Failure -> {
                binding.profileProgressBar.isVisible = false
                snackBar(value.message ?: "Unknown error occurred!")
            }
        }
    }

    private fun setupUnFollowUi() {
        binding.profileHeader.btnProfileFollow.apply {
            text = "Follow"
            setTextColor(resources.getColor(R.color.white, null))
        }
    }

    private fun setupFollowUi() {
        binding.profileHeader.btnProfileFollow.apply {
            text = "Unfollow"
            setTextColor(resources.getColor(R.color.white, null))
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnProfileFollow -> viewModel.toggleFollowUser(uid)
        }
    }
}