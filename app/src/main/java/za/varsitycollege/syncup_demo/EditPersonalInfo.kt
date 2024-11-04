package za.varsitycollege.syncup_demo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditPersonalInfo : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var studentNumberInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneNumberInput: EditText // Optional phone number field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal_info)

        // Initialize input fields
        nameInput = findViewById(R.id.nameInput)
        surnameInput = findViewById(R.id.surnameInput)
        studentNumberInput = findViewById(R.id.studentNumberInput)
        emailInput = findViewById(R.id.emailInput)
        phoneNumberInput = findViewById(R.id.phoneNumberInput)

        // Save button
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            savePersonalInfo()
        }

        // Close button
        findViewById<Button>(R.id.closeButton).setOnClickListener {
            finish() // Close the activity
        }

        // Load existing user data
        loadUserData()
    }

    private fun loadUserData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    val user = task.result
                    nameInput.setText(user.child("name").value.toString())
                    surnameInput.setText(user.child("surname").value.toString())
                    studentNumberInput.setText(user.child("studentNumber").value.toString())
                    emailInput.setText(user.child("email").value.toString())
                    phoneNumberInput.setText(user.child("phone").value.toString()) // Optional
                } else {
                    Toast.makeText(this, "Failed to load user data.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePersonalInfo() {
        val name = nameInput.text.toString().trim()
        val surname = surnameInput.text.toString().trim()
        val studentNumber = studentNumberInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val phone = phoneNumberInput.text.toString().trim() // Optional

        if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() || email.isEmpty()) {
            Snackbar.make(nameInput, "All fields except phone are required.", Snackbar.LENGTH_LONG).show()
            return
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

            val userUpdates = mapOf(
                "name" to name,
                "surname" to surname,
                "studentNumber" to studentNumber,
                "email" to email,
                "phone" to phone // Optional field
            )

            userRef.updateChildren(userUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(nameInput, "Personal info updated successfully.", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(nameInput, "Failed to update personal info.", Snackbar.LENGTH_LONG).show()
                }
            }
        } else {
            Snackbar.make(nameInput, "User not logged in.", Snackbar.LENGTH_LONG).show()
        }
    }
}
