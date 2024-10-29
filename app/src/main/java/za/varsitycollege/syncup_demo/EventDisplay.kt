package za.varsitycollege.syncup_demo

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventDisplay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_display)

        // Get event details from the intent
        val eventId = intent.getStringExtra("eventId") // Ensure eventId is passed in intent
        val eventName = intent.getStringExtra("eventName")
        val eventTime = intent.getStringExtra("eventTime")
        val eventLocation = intent.getStringExtra("eventLocation")
        val eventGenre = intent.getStringExtra("eventGenre") // Get the genre
        val eventPrice = intent.getStringExtra("ticketPrice")
        val djDetails = intent.getParcelableArrayListExtra<DJDetails>("djDetails")

        // Set event details to views
        val eventNameView = findViewById<TextView>(R.id.eventTitle)
        val eventTimeView = findViewById<TextView>(R.id.eventTime)
        val eventLocationView = findViewById<TextView>(R.id.eventLocation)
        val eventGenreView = findViewById<TextView>(R.id.eventGenre)
        val eventPriceView = findViewById<TextView>(R.id.eventPrice)
        val djListContainer = findViewById<LinearLayout>(R.id.djListContainer) // Container for DJ details

        eventNameView.text = eventName
        eventTimeView.text = eventTime
        eventLocationView.text = eventLocation
        eventGenreView.text = eventGenre
        eventPriceView.text = eventPrice

        // Display DJ details if available
        if (djDetails != null && djDetails.isNotEmpty()) {
            djDetails.forEach { dj ->
                val djTextView = TextView(this)
                djTextView.text = "${dj.name} - ${dj.time}"  // Format: "DJ Name - DJ Time"
                djTextView.textSize = 16f
                djListContainer.addView(djTextView)
            }
        } else {
            val noDJTextView = TextView(this)
            noDJTextView.text = "No DJ details available."
            noDJTextView.textSize = 16f
            djListContainer.addView(noDJTextView)
        }

        // Close button action
        val closeButton = findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            finish() // Close the activity
        }
    }
}