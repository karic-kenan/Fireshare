/*
 * Created by Karic Kenan on 6.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.discovery.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentDiscoveryBinding
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.discovery.adapter.UserAdapter
import io.aethibo.fireshare.ui.discovery.viewmodel.DiscoveryViewModel
import io.aethibo.fireshare.ui.othersprofile.view.OthersProfileFragment
import io.aethibo.fireshare.ui.utils.hideKeyboard
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class DiscoveryFragment : Fragment(R.layout.fragment_discovery), SearchView.OnQueryTextListener {

    private val binding: FragmentDiscoveryBinding by viewBinding()
    private val viewModel: DiscoveryViewModel by viewModel()

    private val userAdapter: UserAdapter by lazy { UserAdapter() }
    private var searchJob: Job? = null
    private lateinit var navigator: BottomNavigator

    companion object {
        fun newInstance() = DiscoveryFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigator = BottomNavigator.provide(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Menu
        setHasOptionsMenu(true)

        // Hide soft keyboard
        hideKeyboard(requireActivity())

        setupAdapter()
        setupAdapterClickListeners()
        subscribeToObservers()
    }

    private fun setupAdapterClickListeners() {
        userAdapter.setOnUserClickListener { navigator.addFragment(OthersProfileFragment.newInstance(it.uid)) }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.searchResults.collectLatest { value: Resource<List<User>> ->
                when (value) {
                    is Resource.Init -> Timber.d("Init of user search")
                    is Resource.Loading -> {
                        binding.searchProgressBar.isVisible = true
                        binding.tvSearchPlaceholder.isVisible = false
                    }
                    is Resource.Success -> {
                        binding.searchProgressBar.isVisible = false
                        binding.tvSearchPlaceholder.isVisible = false

                        val result = value.data as List<User>
                        userAdapter.submitList(result)
                    }
                    is Resource.Failure -> {
                        binding.searchProgressBar.isVisible = false
                        binding.tvSearchPlaceholder.isVisible = true
                        snackBar(value.message ?: "Unknown error occurred")
                    }
                }
            }
        }
    }

    private fun setupAdapter() = binding.rvSearchList.apply {
        itemAnimator = null
        adapter = userAdapter
    }

    private fun searchForUsers(query: String) {
        // Make sure we cancel the previous job before creating new one
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            viewModel.searchUser(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchForUsers(query)
            hideKeyboard()
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean = true
}