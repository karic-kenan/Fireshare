/*
 * Created by Karic Kenan on 1.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.framework.di

import io.aethibo.fireshare.data.remote.auth.AuthRemoteDataSource
import io.aethibo.fireshare.data.remote.main.MainRemoteDataSource
import io.aethibo.fireshare.framework.datasource.auth.AuthenticationDataSourceImpl
import io.aethibo.fireshare.framework.datasource.main.MainRemoteDataSourceImpl
import org.koin.dsl.module

val dataSourcesModule = module {
    /**
     * Authentication
     */
    single<AuthRemoteDataSource> { AuthenticationDataSourceImpl() }

    /**
     * Main
     */
    single<MainRemoteDataSource> { MainRemoteDataSourceImpl() }
}