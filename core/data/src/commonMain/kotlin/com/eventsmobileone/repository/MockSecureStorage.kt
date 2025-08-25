package com.eventsmobileone.repository

import com.eventsmobileone.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MockSecureStorage : SecureStorage {
    private var currentUser: User? = null
    private var accessToken: String? = null
    private var refreshToken: String? = null
    
    override suspend fun saveUser(user: User) {
        currentUser = user
    }
    
    override suspend fun getUser(): User? {
        return currentUser
    }
    
    override suspend fun clearUser() {
        currentUser = null
    }
    
    override suspend fun saveAccessToken(token: String) {
        accessToken = token
    }
    
    override suspend fun getAccessToken(): String? {
        return accessToken
    }
    
    override suspend fun clearAccessToken() {
        accessToken = null
    }
    
    override suspend fun saveRefreshToken(token: String) {
        refreshToken = token
    }
    
    override suspend fun getRefreshToken(): String? {
        return refreshToken
    }
    
    override suspend fun clearRefreshToken() {
        refreshToken = null
    }
}
