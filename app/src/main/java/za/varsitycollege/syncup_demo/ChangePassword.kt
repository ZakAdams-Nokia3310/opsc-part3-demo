package za.varsitycollege.syncup_demo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.ChangePasswordRequest
import za.varsitycollege.syncup_demo.network.ChangePasswordResponse

class ChangePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val oldPasswordInput = findViewById<TextInputEditText>(R.id.oldPassword)
        val newPasswordInput = findViewById<TextInputEditText>(R.id.newPassword)
        val confirmNewPasswordInput = findViewById<TextInputEditText>(R.id.confirmNewPassword)
        val saveButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.saveButton)

        // Assuming the username is retrieved from shared preferences or a session manager
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        saveButton.setOnClickListener {
            val oldPassword = oldPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmNewPassword = confirmNewPasswordInput.text.toString()

            // Validate passwords
            if (newPassword != confirmNewPassword) {
                Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
            } else if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Call the function to change the password via API
                changePassword(username, oldPassword, newPassword)
            }
        }

        // Close button click listener
        val closeButton = findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changePassword(username: String, oldPassword: String, newPassword: String) {
        val authService = RetrofitClient.getAuthService()

        val changePasswordRequest = ChangePasswordRequest(username, oldPassword, newPassword)

        authService.changePassword(changePasswordRequest).enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val changePasswordResponse = response.body()!!

                    if (changePasswordResponse.success) {
                        Toast.makeText(this@ChangePassword, "Password changed successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ChangePassword, UserProfile::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@ChangePassword, changePasswordResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ChangePassword, "Failed to change password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Toast.makeText(this@ChangePassword, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
