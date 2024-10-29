package za.varsitycollege.syncup_demo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.GenreRequest
import za.varsitycollege.syncup_demo.network.GenreResponse

class GenreSelection : AppCompatActivity() {

    private val selectedGenres = mutableListOf<String>()  // List to track selected genres

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_selection)

        // Fetching views and genres
        val genreMap = mapOf(
            R.id.cardRNB to Pair("RnB", findViewById<TextView>(R.id.textRNB)),
            R.id.cardPop to Pair("Pop", findViewById<TextView>(R.id.textPop)),
            R.id.cardHipHop to Pair("HipHop", findViewById<TextView>(R.id.textHipHop)),
            R.id.cardIndie to Pair("Indie", findViewById<TextView>(R.id.textIndie)),
            R.id.cardRock to Pair("Rock", findViewById<TextView>(R.id.textRock)),
            R.id.cardAlternative to Pair("Alternative", findViewById<TextView>(R.id.textAlternative)),
            R.id.cardOldSkool to Pair("OldSkool", findViewById<TextView>(R.id.textOldSkool)),
            R.id.cardClassical to Pair("Classical", findViewById<TextView>(R.id.textClassical)),
            R.id.cardJazz to Pair("Jazz", findViewById<TextView>(R.id.textJazz)),
            R.id.cardElectronic to Pair("Electronic", findViewById<TextView>(R.id.textElectronic)),
            R.id.cardReggae to Pair("Reggae", findViewById<TextView>(R.id.textReggae))
        )

        // Setting up click listeners
        genreMap.forEach { (cardId, genrePair) ->
            val cardView = findViewById<CardView>(cardId)
            val genreName = genrePair.first
            val textView = genrePair.second

            cardView.setOnClickListener {
                toggleCardSelection(cardView, textView, genreName)
            }
        }

        // Submit button to send genres to API
        val submitButton = findViewById<Button>(R.id.saveButton)
        submitButton.setOnClickListener {
            submitSelectedGenres()
        }
    }

    private fun toggleCardSelection(cardView: CardView, textView: TextView, genre: String) {
        if (cardView.cardBackgroundColor.defaultColor == Color.WHITE) {
            cardView.setCardBackgroundColor(Color.parseColor("#7D32A8")) // Selected color
            textView.setTextColor(Color.WHITE)
            selectedGenres.add(genre)  // Add genre to selected list
        } else {
            cardView.setCardBackgroundColor(Color.WHITE) // Unselected color
            textView.setTextColor(Color.parseColor("#7D32A8"))
            selectedGenres.remove(genre)  // Remove genre from selected list
        }
    }

    private fun submitSelectedGenres() {
        if (selectedGenres.isEmpty()) {
            Toast.makeText(this, "Please select at least one genre", Toast.LENGTH_SHORT).show()
            return
        }

        // Assuming user is logged in and we can get the username from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Error: No user logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the GenreRequest object
        val genreRequest = GenreRequest(
            username = username,
            selectedGenres = selectedGenres
        )

        // Send the selected genres through the API
        val authService = RetrofitClient.getAuthService()
        authService.submitGenres(genreRequest).enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val genreResponse = response.body()!!
                    if (genreResponse.success) {
                        Toast.makeText(this@GenreSelection, "Genres submitted successfully!", Toast.LENGTH_SHORT).show()

                        // Navigate to the Dashboard activity
                        val intent = Intent(this@GenreSelection, Dashboard::class.java)
                        startActivity(intent)
                        finish()  // Close current activity
                    } else {
                        Toast.makeText(this@GenreSelection, genreResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GenreSelection, "Failed to submit genres", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                Toast.makeText(this@GenreSelection, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
