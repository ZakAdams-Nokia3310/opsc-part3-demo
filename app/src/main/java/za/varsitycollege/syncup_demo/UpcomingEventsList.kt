package za.varsitycollege.syncup_demo

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingEventsList : AppCompatActivity() {

    private lateinit var upcomingEventsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_events_list)

        upcomingEventsContainer = findViewById(R.id.upcomingEventsContainer)

        // Fetch and display the upcoming events from the API
        fetchUpcomingEvents()
    }

    private fun fetchUpcomingEvents() {
        val eventService = RetrofitClient.getEventService()
        eventService.getUpcomingEvents().enqueue(object : Callback<List<EventData>> {
            override fun onResponse(call: Call<List<EventData>>, response: Response<List<EventData>>) {
                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()!!
                    displayEvents(events)
                } else {
                    Toast.makeText(this@UpcomingEventsList, "Failed to load events.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EventData>>, t: Throwable) {
                Toast.makeText(this@UpcomingEventsList, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayEvents(events: List<EventData>) {
        upcomingEventsContainer.removeAllViews() // Clear old events
        for (event in events) {
            addEventCard(event)
        }
    }

    private fun addEventCard(event: EventData) {
        val inflater = layoutInflater
        val eventCardView = inflater.inflate(R.layout.event_card, upcomingEventsContainer, false)

        // Fill in event details in the card view
        eventCardView.findViewById<TextView>(R.id.eventName).text = event.eventName
        eventCardView.findViewById<TextView>(R.id.eventTime).text = event.eventTime
        eventCardView.findViewById<TextView>(R.id.eventLocation).text = event.eventLocation
        eventCardView.findViewById<TextView>(R.id.eventTicketPrice).text = event.ticketPrice

        // Add the card to the container
        upcomingEventsContainer.addView(eventCardView)
    }
}
