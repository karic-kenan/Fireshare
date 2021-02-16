/*
 * Created by Karic Kenan on 8.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.profile.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentProfileBinding
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.FirebaseUtil.auth
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.base.BasePostViewModel
import io.aethibo.fireshare.ui.base.BaseProfilePostFragment
import io.aethibo.fireshare.ui.profile.viewmodel.ProfileViewModel
import io.aethibo.fireshare.ui.settings.view.SettingsFragment
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

open class ProfileFragment : BaseProfilePostFragment(R.layout.fragment_profile) {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val binding: FragmentProfileBinding by viewBinding()

    override val baseViewModel: BasePostViewModel
        get() {
            val viewModel: ProfileViewModel by viewModel()
            return viewModel
        }

    protected val viewModel: ProfileViewModel
        get() = baseViewModel as ProfileViewModel

    protected val uid: String
        get() = auth.uid!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.loadProfile(uid)

        subscribeToObservers()
        setupAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
            inflater.inflate(R.menu.profile_menu, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> navigator.addFragment(SettingsFragment.newInstance())
            R.id.menu_sign_out -> {
            } // TODO: Log out user
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAdapter() {
        lifecycleScope.launchWhenResumed {
            viewModel.getPagingFlow(uid).collect {
                profilePostAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenResumed {
            profilePostAdapter.loadStateFlow.collectLatest {
                if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                    binding.profileProgressBar.isVisible =
                            it.refresh is LoadState.Loading || it.append is LoadState.Loading
                }
            }
        }

        binding.rvProfilePosts.apply {
            adapter = profilePostAdapter
            itemAnimator = null
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.profileMeta.collect { resource: Resource<User> ->
                when (resource) {
                    is Resource.Init -> Timber.d("Fetching user metadata")
                    is Resource.Loading -> binding.profileProgressBar.isVisible = true
                    is Resource.Success -> {
                        binding.profileProgressBar.isVisible = false

                        val data = resource.data as User
                        setupProfileHeader(data)
                    }
                    is Resource.Failure -> {
                        binding.profileProgressBar.isVisible = false
                        snackBar(resource.message ?: "Unknown error occurred")
                    }
                }
            }
        }
    }

    private fun setupProfileHeader(data: User) {
        binding.profileHeader.ivProfileAvatar.load(data.photoUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.profileHeader.tvProfileName.text = data.displayName
        binding.profileHeader.tvProfileUsername.text =
                getString(R.string.label_username, data.username)
        binding.profileHeader.tvProfileBio.text = data.bio
        binding.profileHeader.tvProfileLocation.text = data.location
    }

    override fun onDestroy() {
        Timber.d("FragmentProfile is destroyed")
        super.onDestroy()
    }
}