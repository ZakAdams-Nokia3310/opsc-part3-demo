package za.varsitycollege.syncup_demo.network

data class EventRequest(
    val name: String,
    val date: String,
    val time: String,
    val location: String,
    val ticketPrice: String,
    val djDetails: List<DJDetails>
)

data class DJDetails(
    val name: String,
    val time: String,
    val genre: String
)

data class EventResponse(
    val success: Boolean,
    val message: String,
    val id: String,
    val name: String,
    val date: String,
    val time: String,
    val location: String,
    val ticketPrice: String?,
    val djDetails: List<DJDetails>?
)

