package za.varsitycollege.syncup_demo

data class EventDomain(
    val name: String,
    val location: String,
    val time: String,
    val price: String,
    val genre: String,    // Added genre
)
