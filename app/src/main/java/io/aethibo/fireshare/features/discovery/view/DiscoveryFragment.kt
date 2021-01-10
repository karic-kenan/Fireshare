package io.aethibo.fireshare.features.discovery.view

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
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.core.utils.FirebaseUtil.auth
import io.aethibo.fireshare.databinding.FragmentDiscoveryBinding
import io.aethibo.fireshare.features.discovery.adapter.UserAdapter
import io.aethibo.fireshare.features.discovery.viewmodel.DiscoveryViewModel
import io.aethibo.fireshare.features.profile.view.OthersProfileFragment
import io.aethibo.fireshare.features.utils.hideKeyboard
import io.aethibo.fireshare.features.utils.snackBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class DiscoveryFragment : Fragment(R.layout.fragment_discovery), SearchView.OnQueryTextListener {

    private val binding: FragmentDiscoveryBinding by viewBinding()
    private val viewModel: DiscoveryViewModel by viewModel()
    private val userAdapter: UserAdapter by lazy { UserAdapter() }

    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupClickListeners()
        setupRecyclerView()
        subscribeToObservables()
    }

    private fun setupRecyclerView() = binding.rvSearchList.apply {
        adapter = userAdapter
    }

    private fun subscribeToObservables() {
        viewModel.searchResults.observe(viewLifecycleOwner, EventObserver(
            onLoading = {
                binding.searchProgressBar.isVisible = true
            },
            onSuccess = { users ->
                binding.searchProgressBar.isVisible = false
                binding.tvSearchPlaceholder.isVisible = false
                userAdapter.users = users
            },
            onError = {
                binding.searchProgressBar.isVisible = false
                snackBar(it)
            }
        ))
    }

    private fun setupClickListeners() {
        userAdapter.setOnUserClickListener { user ->
            if (auth.uid == user.uid) {
                requireActivity().nav_view.selectedItemId = R.id.profile
                return@setOnUserClickListener
            }

            BottomNavigator.provide(requireActivity())
                .addFragment(OthersProfileFragment.newInstance(user.uid))
        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating new one
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            delay(500L)
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
            search(query.trim())
            hideKeyboard()
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean = true
}