package za.varsitycollege.syncup_demo.network

data class SubscriptionRequest(
    val userId: String,
    val eventId: String
)

data class SubscriptionResponse(
    val success: Boolean,
    val message: String
)