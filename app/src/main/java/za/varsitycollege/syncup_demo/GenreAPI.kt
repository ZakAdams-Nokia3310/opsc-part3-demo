package za.varsitycollege.syncup_demo.network

data class GenreRequest(
    val username: String,   // or userId, depending on your setup
    val selectedGenres: List<String>
)

data class GenreResponse(
    val success: Boolean,
    val message: String
)
