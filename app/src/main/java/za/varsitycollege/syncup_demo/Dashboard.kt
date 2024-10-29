package za.varsitycollege.syncup_demo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.UserResponse

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val eventContainer = findViewById<LinearLayout>(R.id.eventContainer)
        val greetingTextView = findViewById<TextView>(R.id.greetingTextView) // Assuming you have this TextView in your layout

        // Dummy events data with DJs
        val dummyEvents = arrayListOf(
            EventData(
                eventName = "Music Festival",
                eventTime = "6:00 PM, 10/30/2024",
                eventLocation = "City Stadium",
                ticketPrice = "$30",
                eventGenre = "Rock",
                djDetails = listOf(
                    DJDetails(name = "DJ Rock", time = "6:00 PM"),
                    DJDetails(name = "DJ Beat", time = "7:00 PM")
                )
            ),
            EventData(
                eventName = "Jazz Night",
                eventTime = "8:00 PM, 11/15/2024",
                eventLocation = "Downtown Club",
                ticketPrice = "$20",
                eventGenre = "Jazz",
                djDetails = listOf(
                    DJDetails(name = "DJ Smooth", time = "8:00 PM"),
                    DJDetails(name = "DJ Groove", time = "9:00 PM")
                )

        ),
            EventData(
                eventName = "Electronic Dance Party",
                eventTime = "10:00 PM, 12/01/2024",
                eventLocation = "Open Air Arena",
                ticketPrice = "$50",
                eventGenre = "Electronic",
                djDetails = listOf(
                    DJDetails("DJ Electro", "10:00 PM"),
                    DJDetails("DJ Vibe", "11:00 PM")
                )
            )
        )

        // First, display dummy events
        for (event in dummyEvents) {
            addEventCard(event, eventContainer)
        }

        // Fetch events from API and update UI
        fetchUpcomingEvents(eventContainer)

        // Fetch username and show greeting
        fetchUsername { username ->
            greetingTextView.text = "Hello, $username!"
        }

        // Handle "See All" click to open Upcoming Events List
        val seeAllText = findViewById<TextView>(R.id.textView9)
        seeAllText.setOnClickListener {
            val intent = Intent(this, UpcomingEventsList::class.java)
            intent.putParcelableArrayListExtra("events", dummyEvents) // Include dummy events
            startActivity(intent)
        }
    }

    // Function to fetch username from the database via API
    private fun fetchUsername(callback: (String) -> Unit) {
        val authService = RetrofitClient.getAuthService()

        authService.getUserDetails().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()!!
                    callback(userResponse.username)
                } else {
                    Toast.makeText(this@Dashboard, "Failed to fetch user data.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@Dashboard, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Function to fetch upcoming events from the database
    private fun fetchUpcomingEvents(eventContainer: LinearLayout) {
        val eventService = RetrofitClient.getEventService()

        eventService.getUpcomingEvents().enqueue(object : Callback<List<EventData>> {
            override fun onResponse(call: Call<List<EventData>>, response: Response<List<EventData>>) {
                if (response.isSuccessful && response.body() != null) {
                    val upcomingEvents = response.body()!!

                    // Add fetched events to the container (while keeping dummy events)
                    for (event in upcomingEvents) {
                        addEventCard(event, eventContainer)
                    }
                } else {
                    Toast.makeText(this@Dashboard, "Failed to fetch events.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EventData>>, t: Throwable) {
                Toast.makeText(this@Dashboard, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addEventCard(event: EventData, eventContainer: LinearLayout) {
        // Inflate the event card layout
        val inflater = LayoutInflater.from(this)
        val eventCard = inflater.inflate(R.layout.event_card, eventContainer, false) as CardView

        // Set the event details in the card
        val eventNameView = eventCard.findViewById<TextView>(R.id.eventName)
        val eventTimeView = eventCard.findViewById<TextView>(R.id.eventTime)
        val eventLocationView = eventCard.findViewById<TextView>(R.id.eventLocation)
        val ticketPriceView = eventCard.findViewById<TextView>(R.id.eventTicketPrice)
        val eventGenreView = eventCard.findViewById<TextView>(R.id.eventGenre) // Genre TextView

        eventNameView.text = event.eventName
        eventTimeView.text = event.eventTime
        eventLocationView.text = event.eventLocation
        ticketPriceView.text = event.ticketPrice
        eventGenreView.text = event.eventGenre // Set genre

        // Handle the "View Details" button click
        val viewDetailsButton = eventCard.findViewById<Button>(R.id.viewDetailsButton)
        viewDetailsButton.setOnClickListener {
            // Show full event details
            showEventDetails(event)
        }

        // Add the event card to the event container
        eventContainer.addView(eventCard)
    }


    private fun showEventDetails(event: EventData) {
        // Open the EventDisplay activity to show event details
        val intent = Intent(this, EventDisplay::class.java)
        intent.putExtra("eventName", event.eventName)
        intent.putExtra("eventTime", event.eventTime)
        intent.putExtra("eventLocation", event.eventLocation)
        intent.putExtra("ticketPrice", event.ticketPrice)
        intent.putExtra("eventGenre", event.eventGenre) // Pass genre as well
        startActivity(intent)
    }
}
