package za.varsitycollege.syncup_demo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor

class UserProfile : AppCompatActivity() {

    private lateinit var upcomingEventsContainer: LinearLayout
    private lateinit var attendedEventsContainer: LinearLayout
    private lateinit var profilePicture: ImageView
    private lateinit var biometricExecutor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricManager: BiometricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        upcomingEventsContainer = findViewById(R.id.upcomingEventsContainer)
        attendedEventsContainer = findViewById(R.id.attendedEventsContainer)
        profilePicture = findViewById(R.id.profilePicture)

        biometricManager = BiometricManager.from(this)
        biometricExecutor = ContextCompat.getMainExecutor(this)
        biometricPrompt = createBiometricPrompt()

        setupUI()

        // Fetch subscribed events and categorize them
        fetchUserEvents()
    }

    private fun setupUI() {
        val editProfileBtn = findViewById<Button>(R.id.editProfileBtn)
        editProfileBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        val editPersonalInfoBtn = findViewById<Button>(R.id.editPersonalInfoBtn)
        editPersonalInfoBtn.setOnClickListener {
            val intent = Intent(this, EditPersonalInfo::class.java)
            startActivity(intent)
        }

        val changePasswordBtn = findViewById<Button>(R.id.changePasswordBtn)
        changePasswordBtn.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        val biometricsSwitch = findViewById<Switch>(R.id.biometricsSwitch)
        biometricsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                    biometricPrompt.authenticate(createBiometricPromptInfo())
                } else {
                    Toast.makeText(
                        this,
                        "Biometrics not supported or not set up",
                        Toast.LENGTH_SHORT
                    ).show()
                    biometricsSwitch.isChecked = false
                }
            } else {
                disableBiometrics()
            }
        }

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val imageUri = data?.data
            profilePicture.setImageURI(imageUri)
            // Optionally, upload the profile picture to your backend
        }
    }

    private fun fetchUserEvents() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val eventService = RetrofitClient.getEventService()
        eventService.getSubscribedEvents("Bearer $token")
            .enqueue(object : Callback<List<EventData>> {
                override fun onResponse(
                    call: Call<List<EventData>>,
                    response: Response<List<EventData>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val events = response.body()!!
                        categorizeEvents(events)
                    } else {
                        Toast.makeText(
                            this@UserProfile,
                            "Failed to fetch events.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<EventData>>, t: Throwable) {
                    Toast.makeText(this@UserProfile, "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun categorizeEvents(events: List<EventData>) {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        for (event in events) {
            val eventDate = dateFormat.parse(event.date)
            if (eventDate != null) {
                if (eventDate.after(currentDate)) {
                    addEventToContainer(event, upcomingEventsContainer)
                } else {
                    addEventToContainer(event, attendedEventsContainer)
                }
            }
        }
    }

    private fun addEventToContainer(event: EventData, container: LinearLayout) {
        val eventView = layoutInflater.inflate(R.layout.event_card, container, false)

        val eventName = eventView.findViewById<TextView>(R.id.eventName)
        val eventDate = eventView.findViewById<TextView>(R.id.eventDate)
        val eventTime = eventView.findViewById<TextView>(R.id.eventTime)
        val eventLocation = eventView.findViewById<TextView>(R.id.eventLocation)
        val eventPrice = eventView.findViewById<TextView>(R.id.eventTicketPrice)
        val eventGenre = eventView.findViewById<TextView>(R.id.eventGenre)

        eventName.text = event.name
        eventDate.text = event.date
        eventTime.text = event.time
        eventLocation.text = event.location
        eventPrice.text = event.ticketPrice
        eventGenre.text = event.genre ?: "N/A"  // Handle nullable genre

        container.addView(eventView)
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        return BiometricPrompt(this, biometricExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@UserProfile, "Biometrics enabled", Toast.LENGTH_SHORT)
                        .show()
                    // Save to shared preferences that biometrics are enabled
                    getSharedPreferences("UserPreferences", MODE_PRIVATE)
                        .edit()
                        .putBoolean("biometrics_enabled", true)
                        .apply()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@UserProfile, "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@UserProfile, "Error: $errString", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun createBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate using your biometrics")
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun disableBiometrics() {
        getSharedPreferences("UserPreferences", MODE_PRIVATE).edit().putBoolean("biometrics_enabled", false).apply()
        Toast.makeText(this, "Biometrics disabled", Toast.LENGTH_SHORT).show()
    }
}
