package za.varsitycollege.syncup_demo.network

data class RegistrationResponse(
    val success: Boolean,
    val message: String?,
    val userId: String?  // Add userId to capture it from the server response
)
