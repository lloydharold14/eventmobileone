package com.eventsmobileone.tickets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eventsmobileone.Event
import com.eventsmobileone.EventLocation
import com.eventsmobileone.EventOrganizer
import com.eventsmobileone.EventPricing
import com.eventsmobileone.Coordinates
import com.eventsmobileone.CurrencyInfo
import com.eventsmobileone.designsystem.AppSpacing
import com.eventsmobileone.domain.tickets.GetEventTickets
import com.eventsmobileone.domain.tickets.PurchaseTickets
import com.eventsmobileone.model.AttendeeInfo
import com.eventsmobileone.model.TicketPurchaseRequest
import com.eventsmobileone.model.TicketType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketPurchaseDemoScreen(
    getEventTickets: GetEventTickets,
    purchaseTickets: PurchaseTickets,
    onNavigateBack: () -> Unit
) {
    var ticketTypes by remember { mutableStateOf<List<TicketType>>(emptyList()) }
    var selectedTicketType by remember { mutableStateOf<TicketType?>(null) }
    var quantity by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    
    // Mock event for testing
    val mockEvent = Event(
        id = "event-006",
        title = "Canadian Business Leadership Summit",
        description = "Connect with Canada's top business leaders, entrepreneurs, and investors.",
        startDate = "2024-06-25T08:00:00Z",
        endDate = "2024-06-27T17:00:00Z",
        location = EventLocation(
            address = "Fairmont Royal York, Toronto, ON",
            city = "Toronto",
            province = "ON",
            country = "Canada",
            coordinates = Coordinates(43.6487, -79.3775)
        ),
        organizer = EventOrganizer(
            id = "org-006",
            name = "Business Leadership Institute",
            email = "info@bli.com"
        ),
        category = "business",
        pricing = EventPricing(
            baseCurrency = "CAD",
            baseAmount = 1299.99,
            displayCurrency = "CAD",
            displayAmount = 1299.99,
            exchangeRate = 1.0,
            availableCurrencies = mapOf(
                "CAD" to CurrencyInfo(1.0, "$1.00"),
                "USD" to CurrencyInfo(0.75, "$0.75")
            )
        ),
        images = emptyList(),
        maxAttendees = 600,
        currentAttendees = 280,
        status = "published",
        features = listOf("networking", "workshops", "keynotes", "exhibition", "catering"),
        tags = listOf("business", "leadership", "networking", "strategy", "innovation")
    )
    
    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            getEventTickets(mockEvent.id)
                .onSuccess { types ->
                    ticketTypes = types
                    isLoading = false
                }
                .onFailure { error ->
                    result = "Error loading tickets: ${error.message}"
                    isLoading = false
                }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ticket Purchase Demo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            // Event Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(AppSpacing.md)
                ) {
                    Text(
                        text = mockEvent.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text(
                        text = "${mockEvent.startDate} - ${mockEvent.endDate}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = mockEvent.location.address,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Ticket Types
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    text = "Available Ticket Types",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                ticketTypes.forEach { ticketType ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (selectedTicketType?.id == ticketType.id) 8.dp else 4.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedTicketType?.id == ticketType.id) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        ),
                        onClick = { selectedTicketType = ticketType }
                    ) {
                        Column(
                            modifier = Modifier.padding(AppSpacing.md)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = ticketType.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = ticketType.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Available: ${ticketType.available}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    text = "$${ticketType.price.amount}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            
            // Quantity Selector
            if (selectedTicketType != null) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(AppSpacing.md)
                    ) {
                        Text(
                            text = "Quantity",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                enabled = quantity > 1
                            ) {
                                Text("-", style = MaterialTheme.typography.titleLarge)
                            }
                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = { 
                                    if (quantity < selectedTicketType!!.maxPerOrder && quantity < selectedTicketType!!.available) {
                                        quantity++ 
                                    }
                                },
                                enabled = quantity < selectedTicketType!!.maxPerOrder && quantity < selectedTicketType!!.available
                            ) {
                                Text("+", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                        Text(
                            text = "Total: $${selectedTicketType!!.price.amount * quantity}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Purchase Button
            Button(
                onClick = {
                    if (selectedTicketType != null) {
                        scope.launch {
                            isLoading = true
                            val request = TicketPurchaseRequest(
                                eventId = mockEvent.id,
                                ticketTypeId = selectedTicketType!!.id,
                                quantity = quantity,
                                attendeeInfo = AttendeeInfo(
                                    firstName = "John",
                                    lastName = "Doe",
                                    email = "john.doe@example.com",
                                    phone = "+1234567890"
                                )
                            )
                            
                            purchaseTickets(request)
                                .onSuccess { response ->
                                    result = "Purchase successful! Order ID: ${response.orderId}\nTickets: ${response.tickets.size}\nTotal: $${response.totalAmount.amount}"
                                    isLoading = false
                                }
                                .onFailure { error ->
                                    result = "Purchase failed: ${error.message}"
                                    isLoading = false
                                }
                        }
                    }
                },
                enabled = selectedTicketType != null && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Purchase Tickets")
                }
            }
            
            // Result
            if (result != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = result!!,
                        modifier = Modifier.padding(AppSpacing.md),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
