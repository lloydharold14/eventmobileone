package com.eventsmobileone

import kotlinx.datetime.Instant

actual fun getCurrentTime(): Instant {
    // TODO: Implement proper iOS time - using a fixed timestamp for now
    return Instant.fromEpochMilliseconds(1704067200000) // 2024-01-01 00:00:00 UTC
}
