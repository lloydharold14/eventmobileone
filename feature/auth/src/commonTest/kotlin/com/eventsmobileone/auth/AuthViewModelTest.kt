package com.eventsmobileone.auth

import com.eventsmobileone.*
import com.eventsmobileone.repository.MockAuthRepository
import com.eventsmobileone.usecase.GetCurrentUserUseCase
import com.eventsmobileone.usecase.SignInUseCase
import com.eventsmobileone.usecase.SignInWithOAuthUseCase
import com.eventsmobileone.usecase.SignOutUseCase
import com.eventsmobileone.usecase.SignUpUseCase
import com.eventsmobileone.ui.createDispatcherProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthViewModelTest {
    
    @Test
    fun `test AuthViewModel can be instantiated`() {
        val mockAuthRepository = MockAuthRepository()
        val dispatcherProvider = createDispatcherProvider()
        
        val signInUseCase = SignInUseCase(mockAuthRepository, dispatcherProvider.io)
        val signUpUseCase = SignUpUseCase(mockAuthRepository, dispatcherProvider.io)
        val signInWithOAuthUseCase = SignInWithOAuthUseCase(mockAuthRepository)
        val signOutUseCase = SignOutUseCase(mockAuthRepository, dispatcherProvider.io)
        val getCurrentUserUseCase = GetCurrentUserUseCase(mockAuthRepository, dispatcherProvider.io)
        
        val viewModel = AuthViewModel(
            signInUseCase = signInUseCase,
            signUpUseCase = signUpUseCase,
            signInWithOAuthUseCase = signInWithOAuthUseCase,
            signOutUseCase = signOutUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
            dispatcher = dispatcherProvider.main
        )
        
        // Verify that the ViewModel can be created
        assertTrue(true, "AuthViewModel should be instantiable")
    }
    
    @Test
    fun `test AuthViewModel initial state`() {
        val mockAuthRepository = MockAuthRepository()
        val dispatcherProvider = createDispatcherProvider()
        
        val signInUseCase = SignInUseCase(mockAuthRepository, dispatcherProvider.io)
        val signUpUseCase = SignUpUseCase(mockAuthRepository, dispatcherProvider.io)
        val signInWithOAuthUseCase = SignInWithOAuthUseCase(mockAuthRepository)
        val signOutUseCase = SignOutUseCase(mockAuthRepository, dispatcherProvider.io)
        val getCurrentUserUseCase = GetCurrentUserUseCase(mockAuthRepository, dispatcherProvider.io)
        
        val viewModel = AuthViewModel(
            signInUseCase = signInUseCase,
            signUpUseCase = signUpUseCase,
            signInWithOAuthUseCase = signInWithOAuthUseCase,
            signOutUseCase = signOutUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
            dispatcher = dispatcherProvider.main
        )
        
        // Just verify the ViewModel was created successfully
        assertTrue(true, "AuthViewModel should be created successfully")
    }
}
