package com.eventsmobileone.tickets.di

import com.eventsmobileone.domain.tickets.GetEventTickets
import com.eventsmobileone.domain.tickets.PurchaseTickets
import com.eventsmobileone.domain.tickets.GetUserTickets
import com.eventsmobileone.tickets.TicketPurchaseViewModel
import org.koin.dsl.module

val ticketsModule = module {
    
    // Use Cases
    factory { GetEventTickets(ticketsRepository = get()) }
    factory { PurchaseTickets(ticketsRepository = get()) }
    factory { GetUserTickets(ticketsRepository = get()) }
    
    // ViewModels
    factory { TicketPurchaseViewModel(
        getEventTickets = get(),
        purchaseTickets = get(),
        dispatchers = get(),
        viewModelScope = get()
    ) }
}

