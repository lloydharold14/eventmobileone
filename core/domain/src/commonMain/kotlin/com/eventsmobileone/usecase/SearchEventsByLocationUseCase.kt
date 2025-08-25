package com.eventsmobileone.usecase

import com.eventsmobileone.*
import com.eventsmobileone.repository.EventsRepository

/**
 * Use case for searching events by location
 */
class SearchEventsByLocationUseCase(
    private val eventsRepository: EventsRepository,
    private val locationService: LocationService
) {
    
    /**
     * Search events by location parameters
     */
    suspend operator fun invoke(params: LocationSearchParams): Result<List<EventWithDistance>> {
        return try {
            // Get search coordinates
            val searchCoordinates = when {
                params.useCurrentLocation -> {
                    if (!locationService.hasLocationPermission()) {
                        return Result.failure(Exception("Location permission not granted"))
                    }
                    locationService.getCurrentLocation()
                }
                params.coordinates != null -> params.coordinates
                params.query != null -> {
                    locationService.geocodeLocation(params.query!!)
                }
                else -> {
                    return Result.failure(Exception("No location specified"))
                }
            }
            
            if (searchCoordinates == null) {
                return Result.failure(Exception("Could not determine location coordinates"))
            }
            
            // Get all events (in a real app, this would be paginated)
            val allEventsResult = eventsRepository.getEvents(EventFilter())
            
            if (allEventsResult.isFailure) {
                return Result.failure(allEventsResult.exceptionOrNull() ?: Exception("Failed to get events"))
            }
            
            val allEvents = allEventsResult.getOrThrow()
            
            // Filter events by distance and add distance information
            val eventsWithDistance = allEvents
                .filter { event ->
                    event.location.coordinates != null
                }
                .map { event ->
                    val distance = event.location.coordinates!!.distanceToMiles(searchCoordinates)
                    EventWithDistance(event, distance)
                }
                .filter { eventWithDistance ->
                    eventWithDistance.distanceMiles != null && 
                    eventWithDistance.distanceMiles!! <= params.radiusMiles
                }
                .sortedBy { it.distanceMiles }
            
            Result.success(eventsWithDistance)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Search events near current location
     */
    suspend fun searchNearMe(radiusMiles: Double = 25.0): Result<List<EventWithDistance>> {
        return invoke(
            LocationSearchParams(
                useCurrentLocation = true,
                radiusMiles = radiusMiles
            )
        )
    }
    
    /**
     * Search events by location query
     */
    suspend fun searchByLocation(
        locationQuery: String,
        radiusMiles: Double = 25.0
    ): Result<List<EventWithDistance>> {
        return invoke(
            LocationSearchParams(
                query = locationQuery,
                radiusMiles = radiusMiles
            )
        )
    }
    
    /**
     * Search events by coordinates
     */
    suspend fun searchByCoordinates(
        coordinates: Coordinates,
        radiusMiles: Double = 25.0
    ): Result<List<EventWithDistance>> {
        return invoke(
            LocationSearchParams(
                coordinates = coordinates,
                radiusMiles = radiusMiles
            )
        )
    }
}
