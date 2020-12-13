package io.aethibo.fireshare.core.di

import io.aethibo.fireshare.domain.auth.AuthUseCase
import io.aethibo.fireshare.domain.auth.IAuthUseCase
import io.aethibo.fireshare.domain.main.IMainUseCase
import io.aethibo.fireshare.domain.main.MainUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single<IAuthUseCase> { AuthUseCase(get()) }
    single<IMainUseCase> { MainUseCase(get()) }
}