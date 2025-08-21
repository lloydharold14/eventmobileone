package com.eventsmobileone.repository

import com.eventsmobileone.*
import kotlinx.coroutines.delay

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
            filteredEvents = filteredEvents.filter { it.categoryId == categoryId }
        }
        
        // Apply search filter
        if (filter.searchQuery.isNotBlank()) {
            val query = filter.searchQuery.lowercase()
            filteredEvents = filteredEvents.filter { event ->
                event.title.lowercase().contains(query) ||
                event.description.lowercase().contains(query) ||
                event.location.lowercase().contains(query) ||
                event.organizer.lowercase().contains(query)
            }
        }
        
        // Apply sorting
        filteredEvents = when (filter.sortBy) {
            SortOption.DATE_ASC -> filteredEvents.sortedBy { it.date }
            SortOption.DATE_DESC -> filteredEvents.sortedByDescending { it.date }
            SortOption.PRICE_ASC -> filteredEvents.sortedBy { it.priceRange.minPrice }
            SortOption.PRICE_DESC -> filteredEvents.sortedByDescending { it.priceRange.maxPrice }
            SortOption.NAME_ASC -> filteredEvents.sortedBy { it.title }
            SortOption.NAME_DESC -> filteredEvents.sortedByDescending { it.title }
        }
        
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
        val featuredEvents = generateMockEvents().filter { it.isFeatured }
        return Result.success(featuredEvents)
    }
    
    private fun generateMockEvents(): List<Event> {
        return listOf(
            Event(
                id = "1",
                title = "Summer Music Festival 2024",
                description = "Join us for the biggest music festival of the summer featuring top artists from around the world.",
                categoryId = "music",
                date = "2024-07-15T19:00:00Z",
                location = "Central Park",
                venue = "Central Park Bandshell",
                priceRange = PriceRange(50.0, 150.0),
                thumbnailUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400&h=300&fit=crop",
                imageUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=800&h=600&fit=crop",
                organizer = "Music Events Inc.",
                capacity = 5000,
                availableTickets = 1200,
                tags = listOf("festival", "live music", "outdoor"),
                isFeatured = true
            ),
            Event(
                id = "2",
                title = "Tech Startup Conference",
                description = "Connect with entrepreneurs, investors, and tech leaders in this comprehensive startup conference.",
                categoryId = "technology",
                date = "2024-06-20T09:00:00Z",
                location = "San Francisco",
                venue = "Moscone Center",
                priceRange = PriceRange(299.0, 599.0),
                thumbnailUrl = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=400&h=300&fit=crop",
                imageUrl = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800&h=600&fit=crop",
                organizer = "TechCon Events",
                capacity = 2000,
                availableTickets = 450,
                tags = listOf("startup", "networking", "innovation"),
                isFeatured = true
            ),
            Event(
                id = "3",
                title = "Local Food & Wine Festival",
                description = "Taste the best local cuisine and wines from regional producers.",
                categoryId = "food",
                date = "2024-08-10T12:00:00Z",
                location = "Downtown Plaza",
                venue = "City Square",
                priceRange = PriceRange(25.0, 75.0),
                thumbnailUrl = "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=400&h=300&fit=crop",
                imageUrl = "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&h=600&fit=crop",
                organizer = "Local Food Association",
                capacity = 3000,
                availableTickets = 2100,
                tags = listOf("food", "wine", "local"),
                isFeatured = false
            ),
            Event(
                id = "4",
                title = "Marathon 2024",
                description = "Annual city marathon with scenic routes and professional timing.",
                categoryId = "sports",
                date = "2024-09-22T07:00:00Z",
                location = "City Center",
                venue = "Starting Line - Main Street",
                priceRange = PriceRange(45.0, 45.0),
                thumbnailUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop",
                imageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&h=600&fit=crop",
                organizer = "City Sports Council",
                capacity = 10000,
                availableTickets = 3500,
                tags = listOf("running", "fitness", "charity"),
                isFeatured = true
            ),
            Event(
                id = "5",
                title = "Art Gallery Opening",
                description = "Exclusive opening of contemporary art exhibition featuring local artists.",
                categoryId = "art",
                date = "2024-07-08T18:00:00Z",
                location = "Modern Art Museum",
                venue = "Main Gallery",
                priceRange = PriceRange(15.0, 15.0),
                thumbnailUrl = "https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=400&h=300&fit=crop",
                imageUrl = "https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=800&h=600&fit=crop",
                organizer = "Modern Art Museum",
                capacity = 500,
                availableTickets = 200,
                tags = listOf("art", "exhibition", "culture"),
                isFeatured = false
            ),
            Event(
                id = "6",
                title = "Business Networking Mixer",
                description = "Professional networking event for business leaders and entrepreneurs.",
                categoryId = "business",
                date = "2024-06-25T19:00:00Z",
                location = "Downtown Business District",
                venue = "Grand Hotel Ballroom",
                priceRange = PriceRange(35.0, 35.0),
                thumbnailUrl = "https://images.unsplash.com/photo-1515187029135-18ee286d815b?w=400&h=300&fit=crop",
                imageUrl = "https://images.unsplash.com/photo-1515187029135-18ee286d815b?w=800&h=600&fit=crop",
                organizer = "Business Network Pro",
                capacity = 300,
                availableTickets = 150,
                tags = listOf("networking", "business", "professional"),
                isFeatured = false
            ),
            Event(
                id = "7",
                title = "Yoga & Wellness Retreat",
                description = "Weekend retreat focused on yoga, meditation, and holistic wellness.",
                categoryId = "health",
                date = "2024-08-03T08:00:00Z",
                location = "Mountain Resort",
                venue = "Wellness Center",
                priceRange = PriceRange(199.0, 399.0),
                thumbnailUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400&h=300&fit=crop",
                imageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800&h=600&fit=crop",
                organizer = "Wellness Retreats",
                capacity = 100,
                availableTickets = 45,
                tags = listOf("yoga", "wellness", "retreat"),
                isFeatured = true
            ),
            Event(
                id = "8",
                title = "Coding Bootcamp Workshop",
                description = "Intensive coding workshop for beginners to learn web development.",
                categoryId = "education",
                date = "2024-07-12T10:00:00Z",
                location = "Tech Learning Center",
                venue = "Computer Lab A",
                priceRange = PriceRange(89.0, 89.0),
                thumbnailUrl = "https://example.com/coding-workshop-thumb.jpg",
                imageUrl = "https://example.com/coding-workshop.jpg",
                organizer = "Code Academy",
                capacity = 50,
                availableTickets = 25,
                tags = listOf("coding", "education", "workshop"),
                isFeatured = false
            )
        )
    }
}
