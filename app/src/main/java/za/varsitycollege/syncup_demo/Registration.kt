package za.varsitycollege.syncup_demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.RegistrationRequest
import za.varsitycollege.syncup_demo.network.RegistrationResponse

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Fetching TextInputLayout components for registration fields
        val nameLayout = findViewById<TextInputLayout>(R.id.nameInput)
        val surnameLayout = findViewById<TextInputLayout>(R.id.surnameInput)
        val studentNumberLayout = findViewById<TextInputLayout>(R.id.studentNumberInput)
        val emailLayout = findViewById<TextInputLayout>(R.id.emailInput)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInput)

        val registerButton = findViewById<Button>(R.id.registerButton)

        // Register button click listener
        registerButton.setOnClickListener {
            val name = nameLayout.editText?.text.toString()
            val surname = surnameLayout.editText?.text.toString()
            val studentNumber = studentNumberLayout.editText?.text.toString()
            val email = emailLayout.editText?.text.toString()
            val password = passwordLayout.editText?.text.toString()

            if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create RegistrationRequest object
            val registrationRequest = RegistrationRequest(
                name = name,
                surname = surname,
                studentNumber = studentNumber,
                email = email,
                password = password
            )

            // Make API call to register the user
            val authService = RetrofitClient.getAuthService()
            authService.register(registrationRequest).enqueue(object : Callback<RegistrationResponse> {
                override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                    if (response.isSuccessful) {
                        val registrationResponse = response.body()

                        if (registrationResponse != null && registrationResponse.success) {
                            // Registration was successful
                            Log.d("Registration", "User registered successfully with email: $email")
                            Toast.makeText(this@Registration, "Registration successful!", Toast.LENGTH_SHORT).show()

                            // Navigate to the next screen
                            val intent = Intent(this@Registration, GenreSelection::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If the success field is false or missing
                            val message = registrationResponse?.message ?: "Registration failed. Please try again."
                            Log.d("Registration", "Registration failed: $message")
                            Toast.makeText(this@Registration, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Server returned an error response
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("Registration", "Server error: $errorMessage")
                        Toast.makeText(this@Registration, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                    Log.e("Registration", "Network error: ${t.message}")
                    Toast.makeText(this@Registration, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
