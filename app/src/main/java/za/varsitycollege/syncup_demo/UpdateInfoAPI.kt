package za.varsitycollege.syncup_demo.network

data class UpdatePersonalInfoRequest(
    val name: String,
    val surname: String,
    val studentNumber: String,
    val email: String,
    val phoneNumber: String
)

data class UpdatePersonalInfoResponse(
    val success: Boolean,
    val message: String
)