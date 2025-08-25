package com.eventsmobileone

import kotlinx.datetime.Instant

actual fun getCurrentTime(): Instant = Instant.fromEpochMilliseconds(System.currentTimeMillis())
