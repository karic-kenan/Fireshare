/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.di

import io.aethibo.fireshare.ui.auth.shared.AuthViewModel
import io.aethibo.fireshare.ui.comments.viewmodel.CommentsViewModel
import io.aethibo.fireshare.ui.createpost.viewmodel.CreatePostViewModel
import io.aethibo.fireshare.ui.discovery.viewmodel.DiscoveryViewModel
import io.aethibo.fireshare.ui.postdetail.viewmodel.DetailPostViewModel
import io.aethibo.fireshare.ui.profile.viewmodel.ProfileViewModel
import io.aethibo.fireshare.ui.settings.viewmodel.SettingsViewModel
import io.aethibo.fireshare.ui.timeline.viewmodel.TimelineViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { CreatePostViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { DetailPostViewModel() }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { CommentsViewModel(get(), get(), get()) }
    viewModel { DiscoveryViewModel(get()) }
    viewModel { TimelineViewModel(get(), get()) }
}