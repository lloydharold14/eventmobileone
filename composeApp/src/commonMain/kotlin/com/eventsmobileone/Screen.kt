package com.eventsmobileone

sealed class Screen {
    data object Login : Screen()
    data object SignUp : Screen()
    data object Events : Screen()
    data object Filter : Screen()
    data object LocationSearch : Screen()
    data class EventDetail(val eventId: String) : Screen()
}
