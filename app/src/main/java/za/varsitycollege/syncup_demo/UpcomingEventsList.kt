package za.varsitycollege.syncup_demo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.FirebaseDatabase

class UpcomingEventsList : AppCompatActivity() {

    private lateinit var upcomingEventsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_events_list)

        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        upcomingEventsContainer = findViewById(R.id.upcomingEventsContainer)

        // Fetch and display the upcoming events from Firebase
        fetchUpcomingEvents()
    }

    // Handle the back button in the action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Close this activity and return to the previous one
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchUpcomingEvents() {
        val eventsRef = FirebaseDatabase.getInstance().getReference("events")

        eventsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val events = mutableListOf<Pair<String, EventData>>()
                snapshot.children.forEach { child ->
                    val event = child.getValue(EventData::class.java)
                    val eventId = child.key.orEmpty()
                    if (event != null) {
                        events.add(Pair(eventId, event))
                    }
                }

                if (events.isNotEmpty()) {
                    displayEvents(events)
                } else {
                    Toast.makeText(
                        this@UpcomingEventsList,
                        "No upcoming events found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@UpcomingEventsList,
                    "Failed to fetch events: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun displayEvents(events: List<Pair<String, EventData>>) {
        upcomingEventsContainer.removeAllViews() // Clear old events
        for ((eventId, event) in events) {
            addEventCard(eventId, event)
        }
    }

    private fun addEventCard(eventId: String, event: EventData) {
        val inflater = LayoutInflater.from(this)
        val eventCard = inflater.inflate(R.layout.event_card, upcomingEventsContainer, false) as CardView

        val eventNameView = eventCard.findViewById<TextView>(R.id.eventName)
        val eventTimeView = eventCard.findViewById<TextView>(R.id.eventTime)
        val eventDateView = eventCard.findViewById<TextView>(R.id.eventDate)
        val eventLocationView = eventCard.findViewById<TextView>(R.id.eventLocation)
        val ticketPriceView = eventCard.findViewById<TextView>(R.id.eventTicketPrice)
        val eventGenreView = eventCard.findViewById<TextView>(R.id.eventGenre)

        eventNameView.text = event.name
        eventTimeView.text = event.time
        eventDateView.text = event.date
        eventLocationView.text = event.location
        ticketPriceView.text = event.ticketPrice
        eventGenreView.text = event.genre ?: "N/A"

        val viewDetailsButton = eventCard.findViewById<Button>(R.id.viewDetailsButton)
        viewDetailsButton.setOnClickListener {
            showEventDetails(eventId, event)
        }

        upcomingEventsContainer.addView(eventCard)
    }

    private fun showEventDetails(eventId: String, event: EventData) {
        val intent = Intent(this, EventDisplay::class.java)
        intent.putExtra("eventId", eventId)
        intent.putExtra("eventName", event.name)
        intent.putExtra("eventTime", event.time)
        intent.putExtra("eventDate", event.date)
        intent.putExtra("eventLocation", event.location)
        intent.putExtra("ticketPrice", event.ticketPrice)
        intent.putExtra("eventGenre", event.genre)
        startActivity(intent)
    }
}
