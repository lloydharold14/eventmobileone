package com.eventsmobileone.repository

import com.eventsmobileone.*
import com.eventsmobileone.repository.EventsRepository
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MockEventsRepository : EventsRepository {
    
    override suspend fun getCategories(): Result<List<EventCategory>> {
        delay(500) // Simulate network delay
        return Result.success(
            listOf(
                EventCategory("music", "Music", "🎵", "#9C27B0"),
                EventCategory("sports", "Sports", "⚽", "#2196F3"),
                EventCategory("business", "Business", "💼", "#FF9800"),
                EventCategory("technology", "Technology", "💻", "#4CAF50"),
                EventCategory("food", "Food & Drink", "🍕", "#F44336"),
                EventCategory("art", "Art & Culture", "🎨", "#E91E63"),
                EventCategory("education", "Education", "📚", "#607D8B"),
                EventCategory("health", "Health & Wellness", "🧘", "#8BC34A")
            )
        )
    }
    
    override suspend fun getEvents(filter: EventFilter): Result<List<Event>> {
        delay(800) // Simulate network delay
        val allEvents = generateMockEvents()
        
        var filteredEvents = allEvents
        
        // Apply category filter
        filter.categoryId?.let { categoryId ->
            filteredEvents = filteredEvents.filter { it.category == categoryId }
        }
        
        // Apply search filter
        if (filter.searchQuery.isNotBlank()) {
            val query = filter.searchQuery.lowercase()
            filteredEvents = filteredEvents.filter { event ->
                event.title.lowercase().contains(query) ||
                event.description.lowercase().contains(query) ||
                event.location.address.lowercase().contains(query) ||
                event.organizer.name.lowercase().contains(query)
            }
        }
        
        // Apply date filter
        filteredEvents = applyDateFilter(filteredEvents, filter)
        
        // Apply price filter
        filteredEvents = applyPriceFilter(filteredEvents, filter)
        
        // Apply availability filter
        filteredEvents = applyAvailabilityFilter(filteredEvents, filter)
        
        // Apply sorting
        filteredEvents = applySorting(filteredEvents, filter.sortBy)
        
        return Result.success(filteredEvents)
    }
    
    override suspend fun getEventById(id: String): Result<Event> {
        delay(300) // Simulate network delay
        val event = generateMockEvents().find { it.id == id }
        return if (event != null) {
            Result.success(event)
        } else {
            Result.failure(Exception("Event not found"))
        }
    }
    
    override suspend fun searchEvents(query: String): Result<List<Event>> {
        return getEvents(EventFilter(searchQuery = query))
    }
    
    override suspend fun getEventsByCategory(categoryId: String): Result<List<Event>> {
        return getEvents(EventFilter(categoryId = categoryId))
    }
    
    override suspend fun getFeaturedEvents(): Result<List<Event>> {
        delay(600) // Simulate network delay
        // For now, return the first 3 events as featured
        val featuredEvents = generateMockEvents().take(3)
        return Result.success(featuredEvents)
    }
    
    private fun applyDateFilter(events: List<Event>, filter: EventFilter): List<Event> {
        return when (filter.dateRange) {
            DateRange.TODAY -> {
                // For now, return all events - will implement proper date filtering later
                events
            }
            DateRange.THIS_WEEK -> {
                // For now, return all events - will implement proper date filtering later
                events
            }
            DateRange.THIS_MONTH -> {
                // For now, return all events - will implement proper date filtering later
                events
            }
            DateRange.NEXT_MONTH -> {
                // For now, return all events - will implement proper date filtering later
                events
            }
            DateRange.CUSTOM -> {
                val startDate = filter.customStartDate
                val endDate = filter.customEndDate
                if (startDate != null && endDate != null) {
                    events.filter { event ->
                        try {
                            val eventDate = Instant.parse(event.startDate).toLocalDateTime(TimeZone.currentSystemDefault()).date
                            eventDate >= startDate && eventDate <= endDate
                        } catch (e: Exception) {
                            false
                        }
                    }
                } else {
                    events
                }
            }
            DateRange.ALL -> events
        }
    }
    
    private fun applyPriceFilter(events: List<Event>, filter: EventFilter): List<Event> {
        return when (filter.priceRange) {
            PriceRangeFilter.FREE -> {
                events.filter { it.pricing.baseAmount == 0.0 }
            }
            PriceRangeFilter.UNDER_50 -> {
                events.filter { it.pricing.baseAmount <= 49.99 }
            }
            PriceRangeFilter.FIFTY_TO_100 -> {
                events.filter { it.pricing.baseAmount >= 50.0 && it.pricing.baseAmount <= 100.0 }
            }
            PriceRangeFilter.OVER_100 -> {
                events.filter { it.pricing.baseAmount >= 100.01 }
            }
            PriceRangeFilter.CUSTOM -> {
                val minPrice = filter.customMinPrice
                val maxPrice = filter.customMaxPrice
                events.filter { event ->
                    val eventPrice = event.pricing.baseAmount
                    
                    when {
                        minPrice != null && maxPrice != null -> 
                            eventPrice >= minPrice && eventPrice <= maxPrice
                        minPrice != null -> eventPrice >= minPrice
                        maxPrice != null -> eventPrice <= maxPrice
                        else -> true
                    }
                }
            }
            PriceRangeFilter.ALL -> events
        }
    }
    
    private fun applyAvailabilityFilter(events: List<Event>, filter: EventFilter): List<Event> {
        return when (filter.availability) {
            AvailabilityFilter.AVAILABLE -> {
                events.filter { it.currentAttendees < it.maxAttendees && (it.maxAttendees - it.currentAttendees) > it.maxAttendees * 0.1 }
            }
            AvailabilityFilter.LIMITED -> {
                events.filter { it.currentAttendees < it.maxAttendees && (it.maxAttendees - it.currentAttendees) <= it.maxAttendees * 0.1 }
            }
            AvailabilityFilter.SOLD_OUT -> {
                events.filter { it.currentAttendees >= it.maxAttendees }
            }
            AvailabilityFilter.ALL -> events
        }
    }
    
    private fun applySorting(events: List<Event>, sortBy: SortOption): List<Event> {
        return when (sortBy) {
            SortOption.DATE_ASC -> events.sortedBy { it.startDate }
            SortOption.DATE_DESC -> events.sortedByDescending { it.startDate }
            SortOption.PRICE_ASC -> events.sortedBy { it.pricing.baseAmount }
            SortOption.PRICE_DESC -> events.sortedByDescending { it.pricing.baseAmount }
            SortOption.NAME_ASC -> events.sortedBy { it.title }
            SortOption.NAME_DESC -> events.sortedByDescending { it.title }
            SortOption.POPULARITY -> events.sortedByDescending { it.currentAttendees }
        }
    }
    
    private fun generateMockEvents(): List<Event> {
        return listOf(
            Event(
                id = "1",
                title = "Summer Music Festival 2024",
                description = "Join us for the biggest music festival of the summer featuring top artists from around the world.",
                startDate = "2024-07-15T19:00:00Z",
                endDate = "2024-07-17T23:00:00Z",
                location = EventLocation(
                    address = "Central Park Bandshell",
                    city = "New York",
                    province = "NY",
                    country = "USA",
                    coordinates = Coordinates(40.7829, -73.9654)
                ),
                organizer = EventOrganizer(
                    id = "org-1",
                    name = "Music Events Inc.",
                    email = "info@musicevents.com"
                ),
                category = "music",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 89.99,
                    displayCurrency = "USD",
                    displayAmount = 89.99,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(89.99, "$89.99 USD"),
                        "CAD" to CurrencyInfo(121.49, "$121.49 CAD"),
                        "EUR" to CurrencyInfo(82.50, "€82.50 EUR")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 5000,
                currentAttendees = 1200,
                tags = listOf("festival", "live music", "outdoor"),
                features = listOf("parking", "food", "drinks", "merchandise")
            ),
            Event(
                id = "2",
                title = "Tech Startup Conference",
                description = "Connect with entrepreneurs, investors, and tech leaders in this comprehensive startup conference.",
                startDate = "2024-06-20T09:00:00Z",
                endDate = "2024-06-22T18:00:00Z",
                location = EventLocation(
                    address = "Moscone Center",
                    city = "San Francisco",
                    province = "CA",
                    country = "USA",
                    coordinates = Coordinates(37.7849, -122.4094)
                ),
                organizer = EventOrganizer(
                    id = "org-2",
                    name = "TechCon Events",
                    email = "info@techcon.com"
                ),
                category = "technology",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 299.0,
                    displayCurrency = "USD",
                    displayAmount = 299.0,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(299.0, "$299.00 USD"),
                        "CAD" to CurrencyInfo(403.65, "$403.65 CAD"),
                        "EUR" to CurrencyInfo(275.08, "€275.08 EUR")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 2000,
                currentAttendees = 450,
                tags = listOf("startup", "networking", "innovation"),
                features = listOf("wifi", "catering", "networking", "workshops")
            ),
            Event(
                id = "3",
                title = "Local Food & Wine Festival",
                description = "Taste the best local cuisine and wines from regional producers.",
                startDate = "2024-08-10T12:00:00Z",
                endDate = "2024-08-12T22:00:00Z",
                location = EventLocation(
                    address = "City Square",
                    city = "Chicago",
                    province = "IL",
                    country = "USA",
                    coordinates = Coordinates(41.8781, -87.6298)
                ),
                organizer = EventOrganizer(
                    id = "org-3",
                    name = "Local Food Association",
                    email = "info@localfood.com"
                ),
                category = "food",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 50.0,
                    displayCurrency = "USD",
                    displayAmount = 50.0,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(50.0, "$50.00 USD"),
                        "CAD" to CurrencyInfo(67.50, "$67.50 CAD"),
                        "EUR" to CurrencyInfo(46.00, "€46.00 EUR")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 3000,
                currentAttendees = 2100,
                tags = listOf("food", "wine", "local"),
                features = listOf("food", "drinks", "live-music", "cooking-demos")
            ),
            Event(
                id = "4",
                title = "Marathon 2024",
                description = "Annual city marathon with scenic routes and professional timing.",
                startDate = "2024-09-22T07:00:00Z",
                endDate = "2024-09-22T15:00:00Z",
                location = EventLocation(
                    address = "Starting Line - Main Street",
                    city = "Boston",
                    province = "MA",
                    country = "USA",
                    coordinates = Coordinates(42.3601, -71.0589)
                ),
                organizer = EventOrganizer(
                    id = "org-4",
                    name = "City Sports Council",
                    email = "info@citysports.com"
                ),
                category = "sports",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 45.0,
                    displayCurrency = "USD",
                    displayAmount = 45.0,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(45.0, "$45.00 USD"),
                        "CAD" to CurrencyInfo(60.75, "$60.75 CAD"),
                        "EUR" to CurrencyInfo(41.40, "€41.40 EUR")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 10000,
                currentAttendees = 3500,
                tags = listOf("running", "fitness", "charity"),
                features = listOf("timing", "medals", "aid-stations", "medical-support")
            ),
            Event(
                id = "5",
                title = "Art Gallery Opening",
                description = "Exclusive opening of contemporary art exhibition featuring local artists.",
                startDate = "2024-07-08T18:00:00Z",
                endDate = "2024-07-08T22:00:00Z",
                location = EventLocation(
                    address = "Main Gallery",
                    city = "Los Angeles",
                    province = "CA",
                    country = "USA",
                    coordinates = Coordinates(34.0522, -118.2437)
                ),
                organizer = EventOrganizer(
                    id = "org-5",
                    name = "Modern Art Museum",
                    email = "info@modernart.com"
                ),
                category = "art",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 15.0,
                    displayCurrency = "USD",
                    displayAmount = 15.0,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(15.0, "$15.00 USD"),
                        "CAD" to CurrencyInfo(20.25, "$20.25 CAD"),
                        "EUR" to CurrencyInfo(13.80, "€13.80 EUR")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 500,
                currentAttendees = 200,
                tags = listOf("art", "exhibition", "culture"),
                features = listOf("guided-tours", "refreshments", "artist-meet-greet")
            ),
            Event(
                id = "6",
                title = "Business Networking Mixer",
                description = "Professional networking event for business leaders and entrepreneurs.",
                startDate = "2024-06-25T19:00:00Z",
                endDate = "2024-06-25T22:00:00Z",
                location = EventLocation(
                    address = "Downtown Business District",
                    city = "Miami",
                    province = "FL",
                    country = "USA",
                    coordinates = Coordinates(25.7617, -80.1918)
                ),
                organizer = EventOrganizer(
                    id = "org-6",
                    name = "Business Network Pro",
                    email = "info@businessnetwork.com"
                ),
                category = "business",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 75.0,
                    displayCurrency = "USD",
                    displayAmount = 75.0,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(75.0, "$75.00 USD"),
                        "CAD" to CurrencyInfo(101.25, "$101.25 CAD"),
                        "EUR" to CurrencyInfo(69.00, "€69.00 EUR")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1556761175-b413da4baf72?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 200,
                currentAttendees = 150,
                tags = listOf("networking", "business", "professional"),
                features = listOf("catering", "networking", "presentations", "business-cards")
            ),
            Event(
                id = "7",
                title = "Yoga & Wellness Retreat",
                description = "Weekend retreat focused on yoga, meditation, and holistic wellness.",
                startDate = "2024-08-03T08:00:00Z",
                endDate = "2024-08-05T18:00:00Z",
                location = EventLocation(
                    address = "Wellness Center",
                    city = "Denver",
                    province = "CO",
                    country = "USA",
                    coordinates = Coordinates(39.7392, -104.9903)
                ),
                organizer = EventOrganizer(
                    id = "org-7",
                    name = "Wellness Retreats",
                    email = "info@wellnessretreats.com"
                ),
                category = "health",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 299.0,
                    displayCurrency = "USD",
                    displayAmount = 299.0,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(299.0, "$299.00 USD"),
                        "CAD" to CurrencyInfo(403.65, "$403.65 CAD"),
                        "EUR" to CurrencyInfo(275.08, "€275.08 EUR")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 100,
                currentAttendees = 0, // Sold out
                tags = listOf("yoga", "wellness", "retreat"),
                features = listOf("accommodation", "meals", "yoga-classes", "meditation")
            ),
            Event(
                id = "8",
                title = "Free Community Concert",
                description = "Free outdoor concert featuring local bands and musicians.",
                startDate = "2024-07-20T16:00:00Z",
                endDate = "2024-07-20T22:00:00Z",
                location = EventLocation(
                    address = "Amphitheater",
                    city = "Seattle",
                    province = "WA",
                    country = "USA",
                    coordinates = Coordinates(47.6062, -122.3321)
                ),
                organizer = EventOrganizer(
                    id = "org-8",
                    name = "Community Arts Council",
                    email = "info@communityarts.com"
                ),
                category = "music",
                pricing = EventPricing(
                    baseCurrency = "USD",
                    baseAmount = 0.0,
                    displayCurrency = "USD",
                    displayAmount = 0.0,
                    exchangeRate = 1.0,
                    availableCurrencies = mapOf(
                        "USD" to CurrencyInfo(0.0, "Free"),
                        "CAD" to CurrencyInfo(0.0, "Free"),
                        "EUR" to CurrencyInfo(0.0, "Free")
                    )
                ),
                images = listOf("https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=800&h=600&fit=crop"),
                status = "published",
                maxAttendees = 2000,
                currentAttendees = 1800,
                tags = listOf("free", "community", "music"),
                features = listOf("outdoor", "family-friendly", "food-trucks", "merchandise")
            )
        )
    }
}
