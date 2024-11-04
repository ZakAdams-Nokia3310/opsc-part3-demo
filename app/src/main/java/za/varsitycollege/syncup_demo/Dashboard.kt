package za.varsitycollege.syncup_demo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.UserResponse

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)

        val eventContainer = findViewById<LinearLayout>(R.id.eventContainer)
        val greetingTextView = findViewById<TextView>(R.id.greetingTextView)

        fetchUpcomingEvents(eventContainer)

        fetchUsername { username ->
            greetingTextView.text = if (username != null) {
                "Hello, $username!"
            } else {
                "Hello, Guest!"
            }
        }

        val seeAllText = findViewById<TextView>(R.id.textView9)
        seeAllText.setOnClickListener {
            val intent = Intent(this, UpcomingEventsList::class.java)
            startActivity(intent)
        }

        // Account Button
        val accountButton = findViewById<ConstraintLayout>(R.id.btnAccount)
        accountButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        // Events For You Button
        val eventsForYouButton = findViewById<ConstraintLayout>(R.id.btnEventsForYou)
        eventsForYouButton.setOnClickListener {
            val intent = Intent(this, EventsForYou::class.java)
            startActivity(intent)
        }

        // Create Event Button
        val createEventButton = findViewById<ConstraintLayout>(R.id.btnCreateEvent)
        createEventButton.setOnClickListener {
            val intent = Intent(this, CreateEvent::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUsername(callback: (String?) -> Unit) {
        val authService = RetrofitClient.getAuthService()
        val userId = getSharedPreferences("UserPreferences", MODE_PRIVATE)
            .getString("userId", "") ?: return
        val token = "Bearer " + getSharedPreferences("UserPreferences", MODE_PRIVATE)
            .getString("token", "")

        authService.getUserDetails(userId, token).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()!!
                    val userName = userResponse.data.name
                    callback(userName)
                } else {
                    Toast.makeText(this@Dashboard, "Failed to fetch user data.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@Dashboard, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchUpcomingEvents(eventContainer: LinearLayout) {
        val eventsRef = FirebaseDatabase.getInstance().getReference("events")

        eventsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                val events = mutableListOf<EventData>()
                snapshot.children.forEach { child ->
                    val event = child.getValue(EventData::class.java)
                    if (event != null) {
                        events.add(event)
                    }
                }
                events.forEach { event ->
                    addEventCard(event, eventContainer)
                }
            } else {
                Toast.makeText(this, "Failed to fetch events: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addEventCard(event: EventData, eventContainer: LinearLayout) {
        val inflater = LayoutInflater.from(this)
        val eventCard = inflater.inflate(R.layout.event_card, eventContainer, false)

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
        eventGenreView.text = event.genre

        val viewDetailsButton = eventCard.findViewById<Button>(R.id.viewDetailsButton)
        viewDetailsButton.setOnClickListener {
            showEventDetails(event)
        }

        eventContainer.addView(eventCard)
    }

    private fun showEventDetails(event: EventData) {
        val intent = Intent(this, EventDisplay::class.java)
        intent.putExtra("eventName", event.name)
        intent.putExtra("eventTime", event.time)
        intent.putExtra("eventDate", event.date)
        intent.putExtra("eventLocation", event.location)
        intent.putExtra("ticketPrice", event.ticketPrice)
        intent.putExtra("eventGenre", event.genre)
        startActivity(intent)
    }
}
