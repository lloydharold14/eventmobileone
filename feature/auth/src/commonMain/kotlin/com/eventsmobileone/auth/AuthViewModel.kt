package com.eventsmobileone.auth

import com.eventsmobileone.*
import com.eventsmobileone.AuthEffect
import com.eventsmobileone.AppError
import com.eventsmobileone.usecase.GetCurrentUserUseCase
import com.eventsmobileone.usecase.SignInUseCase
import com.eventsmobileone.usecase.SignInWithOAuthUseCase
import com.eventsmobileone.usecase.SignOutUseCase
import com.eventsmobileone.usecase.SignUpUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signInWithOAuthUseCase: SignInWithOAuthUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val dispatcher: CoroutineDispatcher
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher)
    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()
    
    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect = _effect.asSharedFlow()
    
    init {
        viewModelScope.launch(dispatcher) {
            checkCurrentUser()
        }
    }
    
    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.SignIn -> signIn(event.email, event.password)
            is AuthUiEvent.SignUp -> signUp(event.request)
            is AuthUiEvent.SignInWithOAuth -> signInWithOAuth(event.provider, event.accessToken, event.userData)
            is AuthUiEvent.ForgotPassword -> forgotPassword(event.email)
            is AuthUiEvent.SignOut -> signOut()
            is AuthUiEvent.CheckCurrentUser -> {
                viewModelScope.launch(dispatcher) {
                    checkCurrentUser()
                }
            }
            is AuthUiEvent.RefreshSession -> refreshSession()
            is AuthUiEvent.ClearError -> clearError()
        }
    }
    
    private fun signIn(email: String, password: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch(dispatcher) {
            try {
                val result = signInUseCase(email, password)
                
                if (result.isSuccess) {
                    val authData = result.getOrThrow()
                    println("DEBUG: Sign in successful! User: ${authData.user.email}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = authData.user,
                        isAuthenticated = true,
                        error = null
                    )
                    
                    if (authData.user.emailVerified) {
                        _effect.emit(AuthEffect.NavigateToHome(authData.user))
                    } else {
                        _effect.emit(AuthEffect.NavigateToOnboarding(authData.user))
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    println("DEBUG: Sign in failed! Error: ${exception?.message}")
                    val error = when (exception) {
                        is AppError -> exception
                        else -> GeneralError.UnknownError(
                            userFriendlyMessage = "Sign in failed. Please try again."
                        )
                    }
                    println("DEBUG: Setting error state in AuthViewModel (sign in): ${error.userFriendlyMessage}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error
                    )
                    println("DEBUG: Emitting ShowError effect (sign in)")
                    _effect.emit(AuthEffect.ShowError(error))
                }
            } catch (e: Exception) {
                val error = when (e) {
                    is AppError -> e
                    else -> GeneralError.UnknownError(
                        userFriendlyMessage = "Sign in failed. Please try again."
                    )
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error
                )
                _effect.emit(AuthEffect.ShowError(error))
            }
        }
    }
    
    private fun signUp(request: AuthRequest) {
        println("DEBUG: SignUp called with email: ${request.email}")
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch(dispatcher) {
            try {
                println("DEBUG: Calling signUpUseCase")
                val result = signUpUseCase(request)
                
                if (result.isSuccess) {
                    val authData = result.getOrThrow()
                    println("DEBUG: Sign up successful! User: ${authData.user.email}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = authData.user,
                        isAuthenticated = true,
                        error = null
                    )
                    
                    _effect.emit(AuthEffect.ShowSuccess("Account created successfully! Please check your email for verification."))
                    _effect.emit(AuthEffect.NavigateToOnboarding(authData.user))
                } else {
                    val exception = result.exceptionOrNull()
                    println("DEBUG: Sign up failed! Error: ${exception?.message}")
                    val error = when (exception) {
                        is AppError -> exception
                        else -> GeneralError.UnknownError(
                            userFriendlyMessage = "Sign up failed. Please try again."
                        )
                    }
                    println("DEBUG: Setting error state in AuthViewModel (sign up): ${error.userFriendlyMessage}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error
                    )
                    println("DEBUG: Emitting ShowError effect (sign up)")
                    _effect.emit(AuthEffect.ShowError(error))
                }
            } catch (e: Exception) {
                val error = when (e) {
                    is AppError -> e
                    else -> GeneralError.UnknownError(
                        userFriendlyMessage = "Sign up failed. Please try again."
                    )
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error
                )
                _effect.emit(AuthEffect.ShowError(error))
            }
        }
    }
    
    private fun signInWithOAuth(provider: OAuthProvider, accessToken: String, userData: OAuthUserData) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch(dispatcher) {
            try {
                val request = OAuthRequest(provider, accessToken, userData)
                val result = signInWithOAuthUseCase(request)
                
                if (result.isSuccess) {
                    val authData = result.getOrThrow()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = authData.user,
                        isAuthenticated = true,
                        error = null
                    )
                    
                    _effect.emit(AuthEffect.ShowSuccess("Successfully signed in with ${provider.name}"))
                    _effect.emit(AuthEffect.NavigateToHome(authData.user))
                } else {
                    val exception = result.exceptionOrNull()
                    val error = when (exception) {
                        is AppError -> exception
                        else -> GeneralError.UnknownError(
                            userFriendlyMessage = "OAuth sign in failed. Please try again."
                        )
                    }
                    println("DEBUG: Setting error state in AuthViewModel (OAuth): ${error.userFriendlyMessage}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error
                    )
                    println("DEBUG: Emitting ShowError effect (OAuth)")
                    _effect.emit(AuthEffect.ShowError(error))
                }
            } catch (e: Exception) {
                val error = when (e) {
                    is AppError -> e
                    else -> GeneralError.UnknownError(
                        userFriendlyMessage = "OAuth sign in failed. Please try again."
                    )
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error
                )
                _effect.emit(AuthEffect.ShowError(error))
            }
        }
    }
    
    private fun forgotPassword(email: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch(dispatcher) {
            try {
                // TODO: Implement forgot password
                _state.value = _state.value.copy(isLoading = false)
                _effect.emit(AuthEffect.ShowSuccess("Password reset email sent to $email"))
            } catch (e: Exception) {
                val error = when (e) {
                    is AppError -> e
                    else -> GeneralError.UnknownError(
                        userFriendlyMessage = "Failed to send reset email"
                    )
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error
                )
                _effect.emit(AuthEffect.ShowError(error))
            }
        }
    }
    
    private fun signOut() {
        _state.value = _state.value.copy(isLoading = true)
        
        viewModelScope.launch(dispatcher) {
            try {
                signOutUseCase()
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = null,
                    isAuthenticated = false,
                    error = null
                )
            } catch (e: Exception) {
                val error = when (e) {
                    is AppError -> e
                    else -> GeneralError.UnknownError(
                        userFriendlyMessage = "Sign out failed"
                    )
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error
                )
                _effect.emit(AuthEffect.ShowError(error))
            }
        }
    }
    
    private fun refreshSession() {
        viewModelScope.launch(dispatcher) {
            checkCurrentUser()
        }
    }
    
    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    private suspend fun checkCurrentUser() {
        try {
            val user = getCurrentUserUseCase()
            if (user != null) {
                _state.value = _state.value.copy(
                    user = user,
                    isAuthenticated = true,
                    error = null
                )
            } else {
                _state.value = _state.value.copy(
                    user = null,
                    isAuthenticated = false,
                    error = null
                )
            }
        } catch (e: Exception) {
            val error = when (e) {
                is AppError -> e
                else -> GeneralError.UnknownError(
                    userFriendlyMessage = "Failed to check current user"
                )
            }
            _state.value = _state.value.copy(
                user = null,
                isAuthenticated = false,
                error = error
            )
        }
    }
}
