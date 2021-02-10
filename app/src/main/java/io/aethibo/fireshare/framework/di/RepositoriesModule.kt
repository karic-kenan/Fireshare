/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.di

import io.aethibo.fireshare.data.remote.auth.AuthRepository
import io.aethibo.fireshare.data.remote.auth.DefaultAuthRepository
import io.aethibo.fireshare.data.remote.main.DefaultMainRepository
import io.aethibo.fireshare.data.remote.main.MainRepository
import org.koin.dsl.module

val repositoriesModule = module {
    /**
     * Authentication
     */
    single<AuthRepository> { DefaultAuthRepository(get()) }

    /**
     * Main
     */
    single<MainRepository> { DefaultMainRepository(get()) }
}