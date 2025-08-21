package com.eventsmobileone

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
