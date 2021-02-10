package io.aethibo.fireshare

import android.app.Application
import io.aethibo.fireshare.framework.di.dataSourcesModule
import io.aethibo.fireshare.framework.di.repositoriesModule
import io.aethibo.fireshare.framework.di.useCasesModule
import io.aethibo.fireshare.framework.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FireshareApp : Application() {

    companion object {
        lateinit var instance: Application
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(dataSourcesModule, repositoriesModule, useCasesModule, viewModelsModule)
        }
    }
}