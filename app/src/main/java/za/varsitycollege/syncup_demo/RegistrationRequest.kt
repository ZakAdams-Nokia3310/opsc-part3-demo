package za.varsitycollege.syncup_demo.network

data class RegistrationRequest(
    val name: String,
    val surname: String,
    val studentNumber: String,
    val email: String,
    val password: String
)
