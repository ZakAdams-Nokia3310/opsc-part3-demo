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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.varsitycollege.syncup_demo.network.UploadProfilePictureResponse
import java.io.File
import java.io.FileOutputStream

class EditUserProfile : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var userNameEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var biometricSwitch: Switch
    private val PICK_IMAGE = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        // Find views
        profileImageView = findViewById(R.id.editProfilePicture)
        userNameEditText = findViewById(R.id.editUserName)
        userEmailEditText = findViewById(R.id.editUserEmail)
        biometricSwitch = findViewById(R.id.biometricSwitch)

        // Change profile picture
        profileImageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE)
        }

        // Handle save changes
        val saveChangesBtn = findViewById<Button>(R.id.saveChangesBtn)
        saveChangesBtn.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val userEmail = userEmailEditText.text.toString()

            // Validate info
            if (userName.isEmpty() || userEmail.isEmpty()) {
                Snackbar.make(it, "All fields are required", Snackbar.LENGTH_LONG).show()
            } else {
                // Save the changes to the profile (you can integrate your backend here)
                Snackbar.make(it, "Changes saved", Snackbar.LENGTH_LONG).show()

                // Upload profile picture if selected
                if (imageUri != null) {
                    uploadProfilePicture(imageUri!!)
                } else {
                    Snackbar.make(it, "No profile picture selected", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        // Handle biometrics toggle
        biometricSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Enable biometric login
                Snackbar.make(biometricSwitch, "Biometric login enabled", Snackbar.LENGTH_LONG).show()
            } else {
                // Disable biometric login
                Snackbar.make(biometricSwitch, "Biometric login disabled", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    // Handle selecting an image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }

    // Function to upload profile picture to the backend
    private fun uploadProfilePicture(imageUri: Uri) {
        val authService = RetrofitClient.getAuthService()

        // Assuming you store userId in SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "") ?: ""

        // Convert Uri to File
        val imageFile = uriToFile(imageUri)

        // Prepare MultipartBody.Part
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        // Prepare userId
        val userIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)

        // Upload the profile picture
        authService.uploadProfilePicture(userIdRequestBody, imagePart).enqueue(object : Callback<UploadProfilePictureResponse> {
            override fun onResponse(call: Call<UploadProfilePictureResponse>, response: Response<UploadProfilePictureResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val uploadResponse = response.body()!!
                    Snackbar.make(profileImageView, uploadResponse.message, Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(profileImageView, "Failed to upload picture", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UploadProfilePictureResponse>, t: Throwable) {
                Snackbar.make(profileImageView, "Error: ${t.message}", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    // Convert Uri to File
    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "profile_picture.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}
