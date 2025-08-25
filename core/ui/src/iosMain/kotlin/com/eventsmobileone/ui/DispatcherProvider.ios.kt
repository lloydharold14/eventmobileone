package com.eventsmobileone.ui

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

private class DispatcherProviderImpl : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.Default
    override val default: CoroutineDispatcher = Dispatchers.Default
}

actual fun createDispatcherProvider(): DispatcherProvider = DispatcherProviderImpl()
