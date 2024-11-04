package za.varsitycollege.syncup_demo

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EventDisplay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_display)

        // Get event details from the intent
        val eventId = intent.getStringExtra("eventId")
        val eventName = intent.getStringExtra("eventName")
        val eventTime = intent.getStringExtra("eventTime")
        val eventLocation = intent.getStringExtra("eventLocation")
        val eventGenre = intent.getStringExtra("eventGenre")
        val eventPrice = intent.getStringExtra("ticketPrice")
        val djDetails = intent.getParcelableArrayListExtra<DJDetails>("djDetails")

        // Set event details to views
        val eventNameView = findViewById<TextView>(R.id.eventTitle)
        val eventTimeView = findViewById<TextView>(R.id.eventTime)
        val eventLocationView = findViewById<TextView>(R.id.eventLocation)
        val eventGenreView = findViewById<TextView>(R.id.eventGenre)
        val eventPriceView = findViewById<TextView>(R.id.eventPrice)
        val djListContainer = findViewById<LinearLayout>(R.id.djListContainer)

        eventNameView.text = eventName
        eventTimeView.text = eventTime
        eventLocationView.text = eventLocation
        eventGenreView.text = eventGenre
        eventPriceView.text = eventPrice

        // Display DJ details
        if (djDetails != null && djDetails.isNotEmpty()) {
            djDetails.forEach { dj ->
                val djTextView = TextView(this)
                djTextView.text = "${dj.name} - ${dj.time}"
                djTextView.textSize = 16f
                djListContainer.addView(djTextView)
            }
        } else {
            val noDJTextView = TextView(this)
            noDJTextView.text = "No DJ details available."
            noDJTextView.textSize = 16f
            djListContainer.addView(noDJTextView)
        }

        // Subscribe button action
        val subscribeButton = findViewById<Button>(R.id.subscribeButton)
        subscribeButton.setOnClickListener {
            if (eventId != null) {
                subscribeToEvent(eventId)
            } else {
                Toast.makeText(this, "Event ID is missing!", Toast.LENGTH_SHORT).show()
            }
        }

        // Close button action
        val closeButton = findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            finish()
        }
    }

    private fun subscribeToEvent(eventId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
            userRef.child("subscribedEvents").child(eventId).setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully subscribed to the event!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to subscribe: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
        }
    }
}
