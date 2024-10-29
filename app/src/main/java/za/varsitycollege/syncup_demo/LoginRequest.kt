package za.varsitycollege.syncup_demo.network

data class LoginRequest(
    val email: String,  // Change from username to email
    val password: String
)
