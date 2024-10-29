package za.varsitycollege.syncup_demo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserProfile : AppCompatActivity() {

    private lateinit var upcomingEventsContainer: LinearLayout
    private lateinit var attendedEventsContainer: LinearLayout
    private lateinit var profilePicture: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Bind the upcoming and attended event containers from the layout
        upcomingEventsContainer = findViewById(R.id.upcomingEventsContainer)
        attendedEventsContainer = findViewById(R.id.attendedEventsContainer)

        // Profile picture
        profilePicture = findViewById(R.id.profilePicture)

        // Edit Profile Button (only change profile picture)
        val editProfileBtn = findViewById<Button>(R.id.editProfileBtn)
        editProfileBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // Edit Personal Information Button
        val editPersonalInfoBtn = findViewById<Button>(R.id.editPersonalInfoBtn)
        editPersonalInfoBtn.setOnClickListener {
            val intent = Intent(this, EditPersonalInfo::class.java)
            startActivity(intent)
        }

        // Change Password Button
        val changePasswordBtn = findViewById<Button>(R.id.changePasswordBtn)
        changePasswordBtn.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        // Biometrics Switch
        val biometricsSwitch = findViewById<Switch>(R.id.biometricsSwitch)
        biometricsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Biometrics Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Biometrics Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Logout Button
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Fetch subscribed events and categorize them
        fetchUserEvents()
    }

    // Handle the result of picking an image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val imageUri = data?.data
            profilePicture.setImageURI(imageUri)
            // Logic to save the profile picture to the backend if needed
        }
    }

    // Fetch subscribed events and separate them into upcoming and attended
    private fun fetchUserEvents() {
        val eventService = RetrofitClient.getEventService()

        // Assume there's an API call to get events the user has subscribed to
        eventService.getSubscribedEvents().enqueue(object : Callback<List<EventData>> {
            override fun onResponse(call: Call<List<EventData>>, response: Response<List<EventData>>) {
                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()!!

                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                    // Separate upcoming and attended events
                    for (event in events) {
                        val eventDate: Date? = dateFormat.parse(event.eventTime)
                        if (eventDate != null) {
                            if (eventDate.after(currentDate)) {
                                // Add to upcoming events container
                                addEventToContainer(event, upcomingEventsContainer)
                            } else {
                                // Add to attended events container
                                addEventToContainer(event, attendedEventsContainer)
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@UserProfile, "Failed to fetch events.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EventData>>, t: Throwable) {
                Toast.makeText(this@UserProfile, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Dynamically add event views to the respective containers
    private fun addEventToContainer(event: EventData, container: LinearLayout) {
        val eventView = layoutInflater.inflate(R.layout.event_card, container, false)

        // Bind event data to the views
        val eventName = eventView.findViewById<TextView>(R.id.eventName)
        val eventDate = eventView.findViewById<TextView>(R.id.eventDate)
        val eventLocation = eventView.findViewById<TextView>(R.id.eventLocation)
        val eventPrice = eventView.findViewById<TextView>(R.id.eventTicketPrice)

        eventName.text = event.eventName

        // Parse and format event date
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.parse(event.eventTime)?.let { dateFormat.format(it) }
        eventDate.text = formattedDate ?: "Unknown Date"

        eventLocation.text = event.eventLocation
        eventPrice.text = event.ticketPrice

        // Add the populated event card to the container
        container.addView(eventView)
    }
}
