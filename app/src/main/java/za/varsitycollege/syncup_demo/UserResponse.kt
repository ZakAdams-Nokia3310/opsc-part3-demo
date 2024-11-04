package za.varsitycollege.syncup_demo.network

data class UserResponse(
    val success: Boolean,
    val data: UserDetails
)

data class UserDetails(
    val name: String
)
