package io.aethibo.fireshare

import android.app.Application
import io.aethibo.fireshare.core.di.repositoriesModule
import io.aethibo.fireshare.core.di.useCasesModule
import io.aethibo.fireshare.core.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FireshareApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(repositoriesModule, useCasesModule, viewModelsModule)
        }
    }
}