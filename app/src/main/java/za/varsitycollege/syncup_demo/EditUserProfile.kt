package za.varsitycollege.syncup_demo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditUserProfile : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var userNameEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var biometricSwitch: Switch
    private val PICK_IMAGE = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        // Initialize views
        profileImageView = findViewById(R.id.editProfilePicture)
        userNameEditText = findViewById(R.id.editUserName)
        userEmailEditText = findViewById(R.id.editUserEmail)
        phoneNumberEditText = findViewById(R.id.phoneNumberInput)
        biometricSwitch = findViewById(R.id.biometricSwitch)

        // Set click listener for changing profile picture
        profileImageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE)
        }

        // Save changes button
        findViewById<Button>(R.id.saveChangesBtn).setOnClickListener {
            saveProfileChanges()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }

    private fun saveProfileChanges() {
        val userName = userNameEditText.text.toString().trim()
        val userEmail = userEmailEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()

        if (userName.isEmpty() || userEmail.isEmpty()) {
            Snackbar.make(profileImageView, "Name and Email are required", Snackbar.LENGTH_LONG).show()
            return
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

            val userUpdates = mapOf(
                "name" to userName,
                "email" to userEmail,
                "phone" to phoneNumber // Optional: can be ignored if empty
            )

            userRef.updateChildren(userUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(profileImageView, "Profile updated successfully", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(profileImageView, "Failed to update profile", Snackbar.LENGTH_LONG).show()
                }
            }
        } else {
            Snackbar.make(profileImageView, "User not logged in", Snackbar.LENGTH_LONG).show()
        }
    }
}
