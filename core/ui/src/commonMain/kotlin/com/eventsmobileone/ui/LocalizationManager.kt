package com.eventsmobileone.ui

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


/**
 * Localization manager for handling multi-language support and regional settings
 * Follows the backend API structure for attendee localization
 */
object LocalizationManager {
    private var currentLocale: String = "en-CA"
    private var currentCurrency: String = "CAD"
    private var currentTimezone: String = "America/Toronto"
    private var currentRegion: String = "CA"
    
    /**
     * Set the current locale
     */
    fun setLocale(locale: String) {
        currentLocale = locale
    }
    
    /**
     * Set the current currency
     */
    fun setCurrency(currency: String) {
        currentCurrency = currency
    }
    
    /**
     * Set the current timezone
     */
    fun setTimezone(timezone: String) {
        currentTimezone = timezone
    }
    
    /**
     * Set the current region
     */
    fun setRegion(region: String) {
        currentRegion = region
    }
    
    /**
     * Get current locale
     */
    fun getCurrentLocale(): String = currentLocale
    
    /**
     * Get current currency
     */
    fun getCurrentCurrency(): String = currentCurrency
    
    /**
     * Get current timezone
     */
    fun getCurrentTimezone(): String = currentTimezone
    
    /**
     * Get current region
     */
    fun getCurrentRegion(): String = currentRegion
    
    /**
     * Format price with currency symbol
     */
    fun formatPrice(amount: Double, currency: String = currentCurrency): String {
        val formattedAmount = if (amount == amount.toInt().toDouble()) {
            amount.toInt().toString()
        } else {
            amount.toString()
        }
        return when (currency) {
            "CAD" -> "C$$formattedAmount"
            "USD" -> "$$formattedAmount"
            "EUR" -> "€$formattedAmount"
            "GBP" -> "£$formattedAmount"
            "JPY" -> "¥${amount.toInt()}"
            "XOF" -> "CFA ${amount.toInt()}"
            "GHS" -> "GH₵$formattedAmount"
            "NGN" -> "₦$formattedAmount"
            "AUD" -> "A$$formattedAmount"
            "MXN" -> "MX$$formattedAmount"
            "BRL" -> "R$$formattedAmount"
            "INR" -> "₹$formattedAmount"
            "SGD" -> "S$$formattedAmount"
            else -> "$formattedAmount $currency"
        }
    }
    
    /**
     * Format date based on locale
     */
    fun formatDate(date: String, format: String = "MMM dd, yyyy"): String {
        // Simple date formatting - in a real app, you'd use a proper date library
        return try {
            // For now, return the date as-is
            // In production, you'd parse the ISO date and format it according to locale
            date
        } catch (e: Exception) {
            date
        }
    }
    
    /**
     * Format date range
     */
    fun formatDateRange(startDate: String, endDate: String): String {
        return if (startDate == endDate) {
            formatDate(startDate)
        } else {
            "${formatDate(startDate)} - ${formatDate(endDate)}"
        }
    }
    
    /**
     * Get localized string based on current locale
     */
    fun getLocalizedString(key: String, default: String = key): String {
        return when (currentLocale) {
            "en-CA" -> getEnglishString(key, default)
            "fr-CA" -> getFrenchString(key, default)
            "es-MX" -> getSpanishString(key, default)
            "pt-BR" -> getPortugueseString(key, default)
            "ja-JP" -> getJapaneseString(key, default)
            "hi-IN" -> getHindiString(key, default)
            else -> getEnglishString(key, default)
        }
    }
    
    /**
     * Get English strings
     */
    private fun getEnglishString(key: String, default: String): String {
        return when (key) {
            "search_events" -> "Search Events"
            "filter_events" -> "Filter Events"
            "book_tickets" -> "Book Tickets"
            "my_bookings" -> "My Bookings"
            "profile" -> "Profile"
            "sign_in" -> "Sign In"
            "sign_up" -> "Sign Up"
            "sign_out" -> "Sign Out"
            "email" -> "Email"
            "password" -> "Password"
            "first_name" -> "First Name"
            "last_name" -> "Last Name"
            "username" -> "Username"
            "phone" -> "Phone"
            "create_account" -> "Create Account"
            "forgot_password" -> "Forgot Password?"
            "continue_with_google" -> "Continue with Google"
            "continue_with_apple" -> "Continue with Apple"
            "continue_with_facebook" -> "Continue with Facebook"
            "continue_with_twitter" -> "Continue with Twitter"
            "all_events" -> "All Events"
            "music" -> "Music"
            "sports" -> "Sports"
            "business" -> "Business"
            "technology" -> "Technology"
            "food" -> "Food"
            "arts" -> "Arts"
            "wellness" -> "Wellness"
            "startup" -> "Startup"
            "film" -> "Film"
            "charity" -> "Charity"
            "free" -> "Free"
            "under_50" -> "Under $50"
            "under_100" -> "Under $100"
            "over_100" -> "Over $100"
            "available" -> "Available"
            "limited" -> "Limited"
            "sold_out" -> "Sold Out"
            "near_me" -> "Near Me"
            "distance" -> "Distance"
            "date" -> "Date"
            "price" -> "Price"
            "popularity" -> "Popularity"
            "clear_filters" -> "Clear Filters"
            "apply_filters" -> "Apply Filters"
            "loading" -> "Loading..."
            "error" -> "Error"
            "retry" -> "Retry"
            "cancel" -> "Cancel"
            "confirm" -> "Confirm"
            "save" -> "Save"
            "edit" -> "Edit"
            "delete" -> "Delete"
            "back" -> "Back"
            "next" -> "Next"
            "previous" -> "Previous"
            "close" -> "Close"
            "done" -> "Done"
            "success" -> "Success"
            "failed" -> "Failed"
            "invalid_email" -> "Invalid email address"
            "invalid_password" -> "Password must be at least 8 characters"
            "passwords_dont_match" -> "Passwords don't match"
            "required_field" -> "This field is required"
            "network_error" -> "Network error. Please check your connection."
            "server_error" -> "Server error. Please try again later."
            "unknown_error" -> "An unknown error occurred"
            "no_events_found" -> "No events found"
            "no_bookings_found" -> "No bookings found"
            "event_details" -> "Event Details"
            "event_location" -> "Event Location"
            "event_date" -> "Event Date"
            "event_time" -> "Event Time"
            "event_organizer" -> "Event Organizer"
            "event_description" -> "Event Description"
            "ticket_types" -> "Ticket Types"
            "select_tickets" -> "Select Tickets"
            "total_amount" -> "Total Amount"
            "promo_code" -> "Promo Code"
            "apply_promo" -> "Apply Promo"
            "payment_method" -> "Payment Method"
            "credit_card" -> "Credit Card"
            "paypal" -> "PayPal"
            "apple_pay" -> "Apple Pay"
            "google_pay" -> "Google Pay"
            "complete_booking" -> "Complete Booking"
            "booking_confirmation" -> "Booking Confirmation"
            "booking_successful" -> "Booking Successful!"
            "booking_failed" -> "Booking Failed"
            "payment_processing" -> "Processing Payment..."
            "payment_successful" -> "Payment Successful!"
            "payment_failed" -> "Payment Failed"
            "qr_code" -> "QR Code"
            "download_tickets" -> "Download Tickets"
            "share_event" -> "Share Event"
            "bookmark_event" -> "Bookmark Event"
            "remove_bookmark" -> "Remove Bookmark"
            "attending" -> "Attending"
            "not_attending" -> "Not Attending"
            "maybe" -> "Maybe"
            "invite_friends" -> "Invite Friends"
            "share_on_social" -> "Share on Social"
            "rate_event" -> "Rate Event"
            "write_review" -> "Write Review"
            "contact_organizer" -> "Contact Organizer"
            "report_issue" -> "Report Issue"
            "privacy_policy" -> "Privacy Policy"
            "terms_of_service" -> "Terms of Service"
            "help_support" -> "Help & Support"
            "about" -> "About"
            "version" -> "Version"
            "settings" -> "Settings"
            "notifications" -> "Notifications"
            "language" -> "Language"
            "currency" -> "Currency"
            "timezone" -> "Timezone"
            "dark_mode" -> "Dark Mode"
            "light_mode" -> "Light Mode"
            "system" -> "System"
            "account" -> "Account"
            "personal_info" -> "Personal Info"
            "security" -> "Security"
            "change_password" -> "Change Password"
            "delete_account" -> "Delete Account"
            "logout" -> "Logout"
            else -> default
        }
    }
    
    /**
     * Get French strings
     */
    private fun getFrenchString(key: String, default: String): String {
        return when (key) {
            "search_events" -> "Rechercher des événements"
            "filter_events" -> "Filtrer les événements"
            "book_tickets" -> "Réserver des billets"
            "my_bookings" -> "Mes réservations"
            "profile" -> "Profil"
            "sign_in" -> "Se connecter"
            "sign_up" -> "S'inscrire"
            "sign_out" -> "Se déconnecter"
            "email" -> "Courriel"
            "password" -> "Mot de passe"
            "first_name" -> "Prénom"
            "last_name" -> "Nom de famille"
            "username" -> "Nom d'utilisateur"
            "phone" -> "Téléphone"
            "create_account" -> "Créer un compte"
            "forgot_password" -> "Mot de passe oublié?"
            "continue_with_google" -> "Continuer avec Google"
            "continue_with_apple" -> "Continuer avec Apple"
            "continue_with_facebook" -> "Continuer avec Facebook"
            "continue_with_twitter" -> "Continuer avec Twitter"
            "all_events" -> "Tous les événements"
            "music" -> "Musique"
            "sports" -> "Sports"
            "business" -> "Affaires"
            "technology" -> "Technologie"
            "food" -> "Nourriture"
            "arts" -> "Arts"
            "wellness" -> "Bien-être"
            "startup" -> "Startup"
            "film" -> "Film"
            "charity" -> "Charité"
            "free" -> "Gratuit"
            "under_50" -> "Moins de 50 $"
            "under_100" -> "Moins de 100 $"
            "over_100" -> "Plus de 100 $"
            "available" -> "Disponible"
            "limited" -> "Limité"
            "sold_out" -> "Épuisé"
            "near_me" -> "Près de moi"
            "distance" -> "Distance"
            "date" -> "Date"
            "price" -> "Prix"
            "popularity" -> "Popularité"
            "clear_filters" -> "Effacer les filtres"
            "apply_filters" -> "Appliquer les filtres"
            "loading" -> "Chargement..."
            "error" -> "Erreur"
            "retry" -> "Réessayer"
            "cancel" -> "Annuler"
            "confirm" -> "Confirmer"
            "save" -> "Enregistrer"
            "edit" -> "Modifier"
            "delete" -> "Supprimer"
            "back" -> "Retour"
            "next" -> "Suivant"
            "previous" -> "Précédent"
            "close" -> "Fermer"
            "done" -> "Terminé"
            "success" -> "Succès"
            "failed" -> "Échec"
            "invalid_email" -> "Adresse courriel invalide"
            "invalid_password" -> "Le mot de passe doit contenir au moins 8 caractères"
            "passwords_dont_match" -> "Les mots de passe ne correspondent pas"
            "required_field" -> "Ce champ est requis"
            "network_error" -> "Erreur réseau. Veuillez vérifier votre connexion."
            "server_error" -> "Erreur serveur. Veuillez réessayer plus tard."
            "unknown_error" -> "Une erreur inconnue s'est produite"
            "no_events_found" -> "Aucun événement trouvé"
            "no_bookings_found" -> "Aucune réservation trouvée"
            "event_details" -> "Détails de l'événement"
            "event_location" -> "Lieu de l'événement"
            "event_date" -> "Date de l'événement"
            "event_time" -> "Heure de l'événement"
            "event_organizer" -> "Organisateur de l'événement"
            "event_description" -> "Description de l'événement"
            "ticket_types" -> "Types de billets"
            "select_tickets" -> "Sélectionner les billets"
            "total_amount" -> "Montant total"
            "promo_code" -> "Code promo"
            "apply_promo" -> "Appliquer le promo"
            "payment_method" -> "Méthode de paiement"
            "credit_card" -> "Carte de crédit"
            "paypal" -> "PayPal"
            "apple_pay" -> "Apple Pay"
            "google_pay" -> "Google Pay"
            "complete_booking" -> "Terminer la réservation"
            "booking_confirmation" -> "Confirmation de réservation"
            "booking_successful" -> "Réservation réussie!"
            "booking_failed" -> "Échec de la réservation"
            "payment_processing" -> "Traitement du paiement..."
            "payment_successful" -> "Paiement réussi!"
            "payment_failed" -> "Échec du paiement"
            "qr_code" -> "Code QR"
            "download_tickets" -> "Télécharger les billets"
            "share_event" -> "Partager l'événement"
            "bookmark_event" -> "Marquer l'événement"
            "remove_bookmark" -> "Retirer le marque-page"
            "attending" -> "Participe"
            "not_attending" -> "Ne participe pas"
            "maybe" -> "Peut-être"
            "invite_friends" -> "Inviter des amis"
            "share_on_social" -> "Partager sur les réseaux sociaux"
            "rate_event" -> "Évaluer l'événement"
            "write_review" -> "Écrire un avis"
            "contact_organizer" -> "Contacter l'organisateur"
            "report_issue" -> "Signaler un problème"
            "privacy_policy" -> "Politique de confidentialité"
            "terms_of_service" -> "Conditions d'utilisation"
            "help_support" -> "Aide et support"
            "about" -> "À propos"
            "version" -> "Version"
            "settings" -> "Paramètres"
            "notifications" -> "Notifications"
            "language" -> "Langue"
            "currency" -> "Devise"
            "timezone" -> "Fuseau horaire"
            "dark_mode" -> "Mode sombre"
            "light_mode" -> "Mode clair"
            "system" -> "Système"
            "account" -> "Compte"
            "personal_info" -> "Informations personnelles"
            "security" -> "Sécurité"
            "change_password" -> "Changer le mot de passe"
            "delete_account" -> "Supprimer le compte"
            "logout" -> "Se déconnecter"
            else -> default
        }
    }
    
    /**
     * Get Spanish strings (placeholder)
     */
    private fun getSpanishString(key: String, default: String): String {
        // Placeholder - would contain Spanish translations
        return default
    }
    
    /**
     * Get Portuguese strings (placeholder)
     */
    private fun getPortugueseString(key: String, default: String): String {
        // Placeholder - would contain Portuguese translations
        return default
    }
    
    /**
     * Get Japanese strings (placeholder)
     */
    private fun getJapaneseString(key: String, default: String): String {
        // Placeholder - would contain Japanese translations
        return default
    }
    
    /**
     * Get Hindi strings (placeholder)
     */
    private fun getHindiString(key: String, default: String): String {
        // Placeholder - would contain Hindi translations
        return default
    }
    
    /**
     * Get localization headers for API requests
     */
    fun getLocalizationHeaders(): Map<String, String> {
        return mapOf(
            "Accept-Language" to currentLocale,
            "X-User-Currency" to currentCurrency,
            "X-User-Region" to currentRegion,
            "X-User-Timezone" to currentTimezone
        )
    }
    
    /**
     * Get current timestamp in ISO format
     */
    fun getCurrentTimestamp(): String {
        return Clock.System.now().toString()
    }
    
    /**
     * Get current date in ISO format
     */
    fun getCurrentDate(): String {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
    }
}
