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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val usernameLayout = findViewById<TextInputLayout>(R.id.usernameInput)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInput)

        val usernameInput = usernameLayout.editText
        val passwordInput = passwordLayout.editText

        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val registerButton = findViewById<TextView>(R.id.registerButton)

        loginButton.setOnClickListener {
            val email = usernameInput?.text.toString().trim()
            val password = passwordInput?.text.toString().trim()

            Log.d("Login", "Login button clicked with email: $email")

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }

        forgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Login", "signInWithEmail:success for email: $email")
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val idToken = tokenTask.result?.token
                            saveUserDetails(email, idToken, user)
                        } else {
                            val error = tokenTask.exception?.message ?: "Unknown error"
                            Toast.makeText(this, "Token generation failed: $error", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val error = task.exception?.message ?: "Unknown error"
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed: $error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserDetails(email: String, idToken: String?, user: FirebaseUser?) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("email", email)
            putString("token", idToken)
            putString("userId", user?.uid)
            apply()
        }

        val intent = Intent(this@Login, Dashboard::class.java)
        startActivity(intent)
        finish()
    }
}
