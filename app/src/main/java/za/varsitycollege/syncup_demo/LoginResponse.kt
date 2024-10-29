package za.varsitycollege.syncup_demo

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? // Token is optional, depending on your API
)
