package za.varsitycollege.syncup_demo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.DJDetails
import za.varsitycollege.syncup_demo.network.EventRequest
import za.varsitycollege.syncup_demo.network.EventResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateEvent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        val numberOfDJsInput = findViewById<EditText>(R.id.numberOfDJs)
        val djDetailsContainer = findViewById<LinearLayout>(R.id.djDetailsContainer)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val eventDateButton = findViewById<Button>(R.id.datePickerBtn)
        val eventDateTextView = findViewById<TextView>(R.id.selectedDateTextView)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Event date picker
        eventDateButton.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    eventDateTextView.text = dateFormat.format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Dynamically update DJ input fields based on number of DJs
        numberOfDJsInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val numDJs = s.toString().toIntOrNull() ?: 0
                djDetailsContainer.removeAllViews()
                if (numDJs > 0) {
                    addDJFields(numDJs, djDetailsContainer)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Save button logic
        saveButton.setOnClickListener {
            val eventName = findViewById<EditText>(R.id.eventName).text.toString()
            val eventTime = findViewById<EditText>(R.id.eventTime).text.toString()
            val eventLocation = findViewById<EditText>(R.id.eventLocation).text.toString()
            val ticketPrice = findViewById<EditText>(R.id.ticketPrice).text.toString()
            val eventDate = eventDateTextView.text.toString()

            val djDetailsList = mutableListOf<DJDetails>()

            for (i in 0 until djDetailsContainer.childCount) {
                val djView = djDetailsContainer.getChildAt(i)
                val djName = djView.findViewById<EditText>(R.id.djName).text.toString()
                val djTime = djView.findViewById<EditText>(R.id.djTime).text.toString()
                val djGenre = djView.findViewById<EditText>(R.id.djGenre).text.toString()

                djDetailsList.add(DJDetails(djName, djTime, djGenre))
            }

            val eventRequest = EventRequest(
                name = eventName,
                date = eventDate,
                time = eventTime,
                location = eventLocation,
                ticketPrice = ticketPrice,
                djDetails = djDetailsList
            )

            // Call API to create the event
            createEvent(eventRequest)
        }
    }

    // Function to create the event using Retrofit API
    private fun createEvent(eventRequest: EventRequest) {
        val eventService = RetrofitClient.getEventService()
        eventService.createEvent(eventRequest).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val eventResponse = response.body()!!
                    if (eventResponse.success) {
                        Toast.makeText(this@CreateEvent, "Event created successfully!", Toast.LENGTH_SHORT).show()

                        // Refresh the Upcoming Events list by navigating back
                        val intent = Intent(this@CreateEvent, UpcomingEventsList::class.java)
                        startActivity(intent)
                        finish() // Close the current activity
                    } else {
                        Toast.makeText(this@CreateEvent, "Failed to create event: ${eventResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CreateEvent, "Failed to create event.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Toast.makeText(this@CreateEvent, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Dynamically add DJ input fields
    private fun addDJFields(numberOfDJs: Int, container: LinearLayout) {
        val inflater = LayoutInflater.from(this)
        for (i in 1..numberOfDJs) {
            val djView = inflater.inflate(R.layout.dj_details_item, container, false)
            val djNameInput = djView.findViewById<EditText>(R.id.djName)
            val djTimeInput = djView.findViewById<EditText>(R.id.djTime)
            val djGenreInput = djView.findViewById<EditText>(R.id.djGenre)
            djNameInput.hint = "DJ Name $i"
            djTimeInput.hint = "DJ Time $i"
            djGenreInput.hint = "DJ Genre $i"
            container.addView(djView)
        }
    }
}
