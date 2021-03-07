/*
 * Created by Karic Kenan on 7.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.othersprofile.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.profile.view.ProfileFragment
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class OthersProfileFragment : ProfileFragment() {

    private lateinit var userId: String

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

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.profileMeta.collect { value: Resource<User> ->
                when (value) {
                    is Resource.Init -> Timber.d("Fetching user metadata")
                    is Resource.Loading -> Timber.d("Loading user metadata fetch")
                    is Resource.Success -> {
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
    }
}