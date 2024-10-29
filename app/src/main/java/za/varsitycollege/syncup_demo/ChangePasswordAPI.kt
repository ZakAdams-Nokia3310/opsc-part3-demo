package za.varsitycollege.syncup_demo.network

data class ChangePasswordRequest(
    val username: String, // Assuming you pass the username, you can modify this as per your API requirement
    val oldPassword: String,
    val newPassword: String
)

data class ChangePasswordResponse(
    val success: Boolean,
    val message: String
)
