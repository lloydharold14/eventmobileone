package com.eventsmobileone.auth.di

import com.eventsmobileone.auth.AuthViewModel
import com.eventsmobileone.ui.createDispatcherProvider
import com.eventsmobileone.usecase.GetCurrentUserUseCase
import com.eventsmobileone.usecase.SignInUseCase
import com.eventsmobileone.usecase.SignInWithOAuthUseCase
import com.eventsmobileone.usecase.SignOutUseCase
import com.eventsmobileone.usecase.SignUpUseCase
import org.koin.dsl.module

val authModule = module {
    // Dispatcher provider
    single { createDispatcherProvider() }
    
    // Use cases
    factory {
        SignInUseCase(
            authRepository = get(),
            dispatcher = get<com.eventsmobileone.ui.DispatcherProvider>().io
        )
    }
    
    factory {
        SignUpUseCase(
            authRepository = get(),
            dispatcher = get<com.eventsmobileone.ui.DispatcherProvider>().io
        )
    }
    
    factory {
        SignOutUseCase(
            authRepository = get(),
            dispatcher = get<com.eventsmobileone.ui.DispatcherProvider>().io
        )
    }
    
    factory {
        GetCurrentUserUseCase(
            authRepository = get(),
            dispatcher = get<com.eventsmobileone.ui.DispatcherProvider>().io
        )
    }
    
    factory {
        SignInWithOAuthUseCase(
            authRepository = get()
        )
    }
    
    // ViewModel
    factory {
        AuthViewModel(
            signInUseCase = get(),
            signUpUseCase = get(),
            signInWithOAuthUseCase = get(),
            signOutUseCase = get(),
            getCurrentUserUseCase = get(),
            dispatcher = get<com.eventsmobileone.ui.DispatcherProvider>().main
        )
    }
}
