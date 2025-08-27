import kotlinx.coroutines.runBlocking
import com.eventsmobileone.AuthRequest
import com.eventsmobileone.repository.AuthApiClientImpl

fun main() = runBlocking {
    println("🔍 Debugging Signup API Call")
    println("================================")
    
    val authApiClient = AuthApiClientImpl()
    
    // Test data
    val testRequest = AuthRequest(
        email = "testuser_${System.currentTimeMillis()}@example.com",
        username = "testuser_${System.currentTimeMillis()}",
        password = "TestPassword123!",
        firstName = "Test",
        lastName = "User",
        acceptTerms = true
    )
    
    println("📤 Request Details:")
    println("   Email: ${testRequest.email}")
    println("   Username: ${testRequest.username}")
    println("   First Name: ${testRequest.firstName}")
    println("   Last Name: ${testRequest.lastName}")
    println("   Accept Terms: ${testRequest.acceptTerms}")
    println("   Phone: ${testRequest.phone}")
    println("   Marketing Consent: ${testRequest.marketingConsent}")
    
    println("\n📡 Making API Call...")
    
    val result = authApiClient.signUp(testRequest)
    
    result.fold(
        onSuccess = { response ->
            println("✅ SUCCESS!")
            println("   Success: ${response.success}")
            println("   Data: ${response.data}")
            println("   Error: ${response.error}")
            println("   Timestamp: ${response.timestamp}")
        },
        onFailure = { error ->
            println("❌ FAILED!")
            println("   Error: ${error.message}")
            println("   Error Type: ${error::class.simpleName}")
            println("   Stack Trace:")
            error.printStackTrace()
        }
    )
    
    println("\n🔍 Debug Complete")
}
