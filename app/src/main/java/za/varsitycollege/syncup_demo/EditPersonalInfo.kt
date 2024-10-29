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
import za.varsitycollege.syncup_demo.network.UpdatePersonalInfoRequest
import za.varsitycollege.syncup_demo.network.UpdatePersonalInfoResponse

class EditPersonalInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal_info)

        val nameInput = findViewById<TextInputEditText>(R.id.nameInput)
        val surnameInput = findViewById<TextInputEditText>(R.id.surnameInput)
        val studentNumberInput = findViewById<TextInputEditText>(R.id.studentNumberInput)
        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val phoneNumberInput = findViewById<TextInputEditText>(R.id.phoneNumberInput)
        val saveButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.saveButton)

        // Assuming the user ID or username is stored in SharedPreferences (or retrieved from elsewhere)
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val surname = surnameInput.text.toString()
            val studentNumber = studentNumberInput.text.toString()
            val email = emailInput.text.toString()
            val phoneNumber = phoneNumberInput.text.toString()

            // Validate info
            if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Call function to update the personal info via API
                updatePersonalInfo(name, surname, studentNumber, email, phoneNumber)
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

    private fun updatePersonalInfo(name: String, surname: String, studentNumber: String, email: String, phoneNumber: String) {
        val authService = RetrofitClient.getAuthService()

        val updateRequest = UpdatePersonalInfoRequest(name, surname, studentNumber, email, phoneNumber)

        authService.updatePersonalInfo(updateRequest).enqueue(object : Callback<UpdatePersonalInfoResponse> {
            override fun onResponse(call: Call<UpdatePersonalInfoResponse>, response: Response<UpdatePersonalInfoResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val updateResponse = response.body()!!
                    if (updateResponse.success) {
                        Toast.makeText(this@EditPersonalInfo, "Personal info updated successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditPersonalInfo, UserProfile::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@EditPersonalInfo, updateResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditPersonalInfo, "Failed to update personal info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdatePersonalInfoResponse>, t: Throwable) {
                Toast.makeText(this@EditPersonalInfo, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
