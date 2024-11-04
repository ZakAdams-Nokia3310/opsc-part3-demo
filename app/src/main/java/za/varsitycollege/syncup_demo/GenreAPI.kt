package za.varsitycollege.syncup_demo.network

data class GenreRequest(
    val userId: String,
    val genres: List<String>
)

data class GenreResponse(
    val success: Boolean,
    val message: String
)
