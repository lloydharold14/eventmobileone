package com.eventsmobileone.tickets

import androidx.compose.runtime.Immutable
import com.eventsmobileone.ui.DispatcherProvider
import com.eventsmobileone.domain.tickets.GetEventTickets
import com.eventsmobileone.domain.tickets.PurchaseTickets
import com.eventsmobileone.Event
import com.eventsmobileone.model.TicketPurchaseRequest
import com.eventsmobileone.model.TicketType
import com.eventsmobileone.model.AttendeeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Immutable
data class TicketPurchaseUiState(
    val event: Event? = null,
    val ticketTypes: List<TicketType> = emptyList(),
    val selectedTicketType: TicketType? = null,
    val quantity: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface TicketPurchaseUiEvent {
    data class LoadEventTickets(val eventId: String) : TicketPurchaseUiEvent
    data class SelectTicketType(val ticketType: TicketType) : TicketPurchaseUiEvent
    data class ChangeQuantity(val quantity: Int) : TicketPurchaseUiEvent
    object PurchaseTickets : TicketPurchaseUiEvent
}

sealed interface TicketPurchaseEffect {
    data class NavigateToPayment(val paymentUrl: String) : TicketPurchaseEffect
    data class NavigateToSuccess(val orderId: String) : TicketPurchaseEffect
    data class ShowError(val message: String) : TicketPurchaseEffect
}

class TicketPurchaseViewModel(
    private val getEventTickets: GetEventTickets,
    private val purchaseTickets: PurchaseTickets,
    private val dispatchers: DispatcherProvider,
    private val viewModelScope: CoroutineScope
) {
    
    private val _state = MutableStateFlow(TicketPurchaseUiState())
    val state: StateFlow<TicketPurchaseUiState> = _state.asStateFlow()
    
    private val _effect = MutableSharedFlow<TicketPurchaseEffect>()
    val effect: SharedFlow<TicketPurchaseEffect> = _effect
    
    fun onEvent(event: TicketPurchaseUiEvent) {
        when (event) {
            is TicketPurchaseUiEvent.LoadEventTickets -> {
                loadEventTickets(event.eventId)
            }
            is TicketPurchaseUiEvent.SelectTicketType -> {
                _state.value = _state.value.copy(
                    selectedTicketType = event.ticketType,
                    quantity = 1 // Reset quantity when ticket type changes
                )
            }
            is TicketPurchaseUiEvent.ChangeQuantity -> {
                _state.value = _state.value.copy(quantity = event.quantity)
            }
            TicketPurchaseUiEvent.PurchaseTickets -> {
                purchaseTickets()
            }
        }
    }
    
    private fun loadEventTickets(eventId: String) {
        viewModelScope.launch(dispatchers.io) {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            getEventTickets(eventId)
                .onSuccess { ticketTypes ->
                    _state.value = _state.value.copy(
                        ticketTypes = ticketTypes,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Failed to load ticket types",
                        isLoading = false
                    )
                }
        }
    }
    
    private fun purchaseTickets() {
        val currentState = _state.value
        val selectedTicketType = currentState.selectedTicketType
        val event = currentState.event
        
        if (selectedTicketType == null || event == null) {
            viewModelScope.launch {
                _effect.emit(TicketPurchaseEffect.ShowError("Please select a ticket type"))
            }
            return
        }
        
        viewModelScope.launch(dispatchers.io) {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            val request = TicketPurchaseRequest(
                eventId = event.id,
                ticketTypeId = selectedTicketType.id,
                quantity = currentState.quantity,
                attendeeInfo = AttendeeInfo(
                    firstName = "John", // TODO: Get from user profile
                    lastName = "Doe",
                    email = "john.doe@example.com",
                    phone = null
                )
            )
            
            purchaseTickets(request)
                .onSuccess { response ->
                    _state.value = _state.value.copy(isLoading = false)
                    
                    val paymentUrl = response.paymentUrl
                    if (paymentUrl != null) {
                        _effect.emit(TicketPurchaseEffect.NavigateToPayment(paymentUrl))
                    } else {
                        _effect.emit(TicketPurchaseEffect.NavigateToSuccess(response.orderId))
                    }
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Failed to purchase tickets",
                        isLoading = false
                    )
                    _effect.emit(TicketPurchaseEffect.ShowError(error.message ?: "Failed to purchase tickets"))
                }
        }
    }
}
