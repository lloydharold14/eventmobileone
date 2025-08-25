package com.eventsmobileone.ui

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

expect fun createDispatcherProvider(): DispatcherProvider
