package com.eventsmobileone.tickets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.eventsmobileone.designsystem.AppSpacing
import com.eventsmobileone.designsystem.AppTypography
import com.eventsmobileone.designsystem.AppRadius
import com.eventsmobileone.designsystem.AppElevation
import com.eventsmobileone.Event
import com.eventsmobileone.model.TicketType
import com.eventsmobileone.model.Money
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketPurchaseScreen(
    event: Event,
    ticketTypes: List<TicketType>,
    selectedTicketType: TicketType?,
    quantity: Int,
    isLoading: Boolean,
    onTicketTypeSelected: (TicketType) -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onPurchaseClicked: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Purchase Tickets") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            // Event Summary
            item {
                EventSummaryCard(event = event)
            }
            
            // Ticket Types
            item {
                Text(
                    text = "Select Ticket Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(ticketTypes) { ticketType ->
                TicketTypeCard(
                    ticketType = ticketType,
                    isSelected = selectedTicketType?.id == ticketType.id,
                    onClick = { onTicketTypeSelected(ticketType) }
                )
            }
            
            // Quantity Selector
            item {
                if (selectedTicketType != null) {
                    QuantitySelector(
                        quantity = quantity,
                        maxQuantity = selectedTicketType.maxPerOrder,
                        available = selectedTicketType.available,
                        onQuantityChanged = onQuantityChanged
                    )
                }
            }
            
            // Purchase Summary
            item {
                if (selectedTicketType != null) {
                    PurchaseSummaryCard(
                        ticketType = selectedTicketType,
                        quantity = quantity
                    )
                }
            }
            
            // Purchase Button
            item {
                PurchaseButton(
                    isEnabled = selectedTicketType != null && quantity > 0 && !isLoading,
                    isLoading = isLoading,
                    onClick = onPurchaseClicked
                )
            }
        }
    }
}

@Composable
private fun EventSummaryCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.sm)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.md)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(AppSpacing.xs))
            Text(
                text = "${event.startDate} - ${event.endDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(AppSpacing.xs))
            Text(
                text = event.location.address,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TicketTypeCard(
    ticketType: TicketType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) AppElevation.md else AppElevation.sm
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
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
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text(
                        text = ticketType.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.sm))
                    Text(
                        text = "Available: ${ticketType.available}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "$${ticketType.price.amount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            if (ticketType.benefits.isNotEmpty()) {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(
                    text = "Benefits:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                ticketType.benefits.forEach { benefit ->
                    Text(
                        text = "• $benefit",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    maxQuantity: Int,
    available: Int,
    onQuantityChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.sm)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.md)
        ) {
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                IconButton(
                    onClick = { if (quantity > 1) onQuantityChanged(quantity - 1) },
                    enabled = quantity > 1
                ) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }
                
                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = AppSpacing.md)
                )
                
                IconButton(
                    onClick = { 
                        if (quantity < maxQuantity && quantity < available) {
                            onQuantityChanged(quantity + 1) 
                        }
                    },
                    enabled = quantity < maxQuantity && quantity < available
                ) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }
            
            Text(
                text = "Max ${maxQuantity} per order • ${available} available",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PurchaseSummaryCard(
    ticketType: TicketType,
    quantity: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.sm)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.md)
        ) {
            Text(
                text = "Purchase Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${ticketType.name} × $quantity",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$${ticketType.price.amount * quantity}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = AppSpacing.sm))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${ticketType.price.amount * quantity}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun PurchaseButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(AppSpacing.md),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Purchase Tickets",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
