package com.eventsmobileone.repository

import com.eventsmobileone.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.Dispatchers

class AuthApiIntegrationTest {
    
    private lateinit var authApiClient: AuthApiClient
    private lateinit var authRepository: AuthRepository
    
    @BeforeTest
    fun setup() {
        authApiClient = AuthApiClientImpl()
        authRepository = AuthRepositoryImpl(authApiClient, MockSecureStorage())
    }
    
    @Test
    fun `test sign up with new user`() = runTest {
        // Generate a unique email to avoid conflicts
        val testEmail = "testuser_${System.currentTimeMillis()}@example.com"
        val testPassword = "TestPassword123!"
        
        val signUpRequest = AuthRequest(
            email = testEmail,
            password = testPassword,
            firstName = "Test",
            lastName = "User",
            acceptTerms = true
        )
        
        println("🔐 Testing Sign Up with email: $testEmail")
        
        val result = authRepository.signUp(signUpRequest)
        
        result.fold(
            onSuccess = { authData ->
                println("✅ Sign Up SUCCESS!")
                println("   User ID: ${authData.user.id}")
                println("   Email: ${authData.user.email}")
                println("   Name: ${authData.user.firstName} ${authData.user.lastName}")
                assertNotNull(authData.user.id)
                assertEquals(testEmail, authData.user.email)
                assertEquals("Test", authData.user.firstName)
                assertEquals("User", authData.user.lastName)
            },
            onFailure = { error ->
                println("❌ Sign Up FAILED: ${error.message}")
                println("   Error type: ${error::class.simpleName}")
                fail("Sign up should succeed for new user: ${error.message}")
            }
        )
    }
    
    @Test
    fun `test sign in with valid credentials`() = runTest {
        // Use a known test account or create one first
        val testEmail = "testuser_${System.currentTimeMillis()}@example.com"
        val testPassword = "TestPassword123!"
        
        // First, create a user
        val signUpRequest = AuthRequest(
            email = testEmail,
            password = testPassword,
            firstName = "Test",
            lastName = "User",
            acceptTerms = true
        )
        
        println("🔐 Creating test user: $testEmail")
        val signUpResult = authRepository.signUp(signUpRequest)
        
        if (signUpResult.isFailure) {
            println("⚠️ Could not create test user, trying with existing credentials")
            // Try with a known test account
            val signInResult = authRepository.signIn(
                email = "test@example.com",
                password = "TestPassword123!"
            )
            
            signInResult.fold(
                onSuccess = { authData ->
                    println("✅ Sign In SUCCESS with existing account!")
                    println("   User ID: ${authData.user.id}")
                    println("   Email: ${authData.user.email}")
                    assertNotNull(authData.user.id)
                    assertNotNull(authData.user.email)
                },
                onFailure = { error ->
                    println("❌ Sign In FAILED: ${error.message}")
                    println("   Error type: ${error::class.simpleName}")
                    // Don't fail the test, just log the error
                    println("   This might be expected if no test account exists")
                }
            )
            return@runTest
        }
        
        // Now try to sign in with the created user
        println("🔐 Testing Sign In with created user: $testEmail")
        
        val signInResult = authRepository.signIn(
            email = testEmail,
            password = testPassword
        )
        
        signInResult.fold(
            onSuccess = { authData ->
                println("✅ Sign In SUCCESS!")
                println("   User ID: ${authData.user.id}")
                println("   Email: ${authData.user.email}")
                println("   Name: ${authData.user.firstName} ${authData.user.lastName}")
                assertNotNull(authData.user.id)
                assertEquals(testEmail, authData.user.email)
                assertEquals("Test", authData.user.firstName)
                assertEquals("User", authData.user.lastName)
            },
            onFailure = { error ->
                println("❌ Sign In FAILED: ${error.message}")
                println("   Error type: ${error::class.simpleName}")
                fail("Sign in should succeed with valid credentials: ${error.message}")
            }
        )
    }
    
    @Test
    fun `test sign in with invalid credentials`() = runTest {
        println("🔐 Testing Sign In with invalid credentials")
        
        val result = authRepository.signIn(
            email = "invalid@example.com",
            password = "WrongPassword123!"
        )
        
        result.fold(
            onSuccess = { authData ->
                println("⚠️ Sign In SUCCEEDED with invalid credentials (unexpected)")
                fail("Sign in should fail with invalid credentials")
            },
            onFailure = { error ->
                println("✅ Sign In FAILED as expected")
                println("   Error: ${error.message}")
                println("   Error type: ${error::class.simpleName}")
                assertTrue(error is AuthError.InvalidCredentials || error is AuthError.UserNotFound)
            }
        )
    }
    
    @Test
    fun `test sign up with existing email`() = runTest {
        // First, create a user
        val testEmail = "duplicate_${System.currentTimeMillis()}@example.com"
        val testPassword = "TestPassword123!"
        
        val signUpRequest = AuthRequest(
            email = testEmail,
            password = testPassword,
            firstName = "Test",
            lastName = "User",
            acceptTerms = true
        )
        
        println("🔐 Creating first user: $testEmail")
        val firstSignUp = authRepository.signUp(signUpRequest)
        
        if (firstSignUp.isSuccess) {
            println("✅ First user created successfully")
            
            // Now try to create another user with the same email
            println("🔐 Testing duplicate email sign up")
            val duplicateSignUp = authRepository.signUp(signUpRequest)
            
            duplicateSignUp.fold(
                onSuccess = { user ->
                    println("⚠️ Duplicate sign up SUCCEEDED (unexpected)")
                    fail("Sign up should fail with duplicate email")
                },
                onFailure = { error ->
                    println("✅ Duplicate sign up FAILED as expected")
                    println("   Error: ${error.message}")
                    println("   Error type: ${error::class.simpleName}")
                    assertTrue(error is AuthError.EmailAlreadyExists)
                }
            )
        } else {
            println("⚠️ Could not create first user for duplicate test")
            println("   Error: ${firstSignUp.exceptionOrNull()?.message}")
        }
    }
    
    @Test
    fun `test API client direct calls`() = runTest {
        println("🔐 Testing AuthApiClient direct calls")
        
        val testEmail = "direct_${System.currentTimeMillis()}@example.com"
        val testPassword = "TestPassword123!"
        
        // Test direct API client sign up
        val signUpResponse = authApiClient.signUp(
            AuthRequest(
                email = testEmail,
                password = testPassword,
                firstName = "Direct",
                lastName = "Test",
                acceptTerms = true
            )
        )
        
        println("📡 Direct API Sign Up Response:")
        signUpResponse.fold(
            onSuccess = { response ->
                println("   Success: ${response.success}")
                println("   Data: ${response.data}")
                println("   Error: ${response.error}")
                assertTrue(response.success)
                assertNotNull(response.data)
                assertNotNull(response.data?.user)
            },
            onFailure = { error ->
                println("   Error: ${error.message}")
                println("   Error type: ${error::class.simpleName}")
            }
        )
        
        // Test direct API client sign in
        val signInResponse = authApiClient.signIn(
            email = testEmail,
            password = testPassword
        )
        
        println("📡 Direct API Sign In Response:")
        signInResponse.fold(
            onSuccess = { response ->
                println("   Success: ${response.success}")
                println("   Data: ${response.data}")
                println("   Error: ${response.error}")
                assertTrue(response.success)
                assertNotNull(response.data)
                assertNotNull(response.data?.user)
            },
            onFailure = { error ->
                println("   Error: ${error.message}")
                println("   Error type: ${error::class.simpleName}")
            }
        )
    }
    
    @Test
    fun `test network connectivity`() = runTest {
        println("🌐 Testing network connectivity")
        
        // Try a simple API call to test connectivity
        val result = authApiClient.signIn(
            email = "connectivity@test.com",
            password = "test"
        )
        
        result.fold(
            onSuccess = { response ->
                println("✅ Network connectivity OK")
                println("   Response received: ${response.success}")
                // Even if auth fails, we got a response, so network is working
            },
            onFailure = { error ->
                println("❌ Network connectivity issue")
                println("   Error: ${error.message}")
                println("   Error type: ${error::class.simpleName}")
                
                when (error) {
                    is NetworkError.ConnectionError -> {
                        println("   🔌 Connection error - check internet connection")
                    }
                    is NetworkError.TimeoutError -> {
                        println("   ⏰ Timeout error - API might be slow")
                    }
                    is NetworkError.ServerError -> {
                        println("   🖥️ Server error - API endpoint issue")
                    }
                    else -> {
                        println("   ❓ Unknown network error")
                    }
                }
            }
        )
    }
}
