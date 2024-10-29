package za.varsitycollege.syncup_demo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.LoginRequest

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Fetching TextInputLayout components
        val usernameLayout = findViewById<TextInputLayout>(R.id.usernameInput)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInput)

        // Getting TextInputEditText from TextInputLayout
        val usernameInput = usernameLayout.editText
        val passwordInput = passwordLayout.editText

        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val registerButton = findViewById<TextView>(R.id.registerButton)

        // Login button click listener
        loginButton.setOnClickListener {
            val email = usernameInput?.text.toString()
            val password = passwordInput?.text.toString()

            // Add logging for debugging
            Log.d("Login", "Login button clicked with email: $email")

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }

        // Forgot password click listener
        forgotPassword.setOnClickListener {
            // TODO: Implement forgot password logic
        }

        // Navigate to registration
        registerButton.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // Function to handle login
    private fun loginUser(email: String, password: String) {
        // Create a login request object
        val loginRequest = LoginRequest(email, password)

        // Make the API call
        val authService = RetrofitClient.getAuthService()
        val call = authService.loginUser(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    if (loginResponse.success) {
                        // Save email and token in SharedPreferences
                        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("email", email)
                        editor.putString("token", loginResponse.token) // Save the custom token
                        editor.apply()

                        // Redirect to Dashboard
                        val intent = Intent(this@Login, Dashboard::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Show error message from the server
                        Toast.makeText(this@Login, "Login failed: ${loginResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // General error message for non-2xx responses
                    Toast.makeText(this@Login, "Error: Could not log in. ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@Login, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
