package za.varsitycollege.syncup_demo.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import za.varsitycollege.syncup_demo.LoginResponse

// Update with your deployed API's URL
private const val BASE_URL = "https://api-twjzcamdda-uc.a.run.app/"  // Replace with actual deployed API URL

interface AuthService {
    @POST("api/auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/auth/register")
    fun register(@Body registrationRequest: RegistrationRequest): Call<RegistrationResponse>

    @POST("api/genres")
    fun submitGenres(@Body genreRequest: GenreRequest): Call<GenreResponse>

    @POST("api/events")
    fun createEvent(@Body eventRequest: EventRequest): Call<EventResponse>

    @GET("api/events") // Adjust based on your API design
    fun getUpcomingEvents(): Call<List<EventResponse>>

    @GET("api/user/details") // Ensure this matches your backend
    fun getUserDetails(): Call<UserResponse>

    @POST("api/changePassword") // Ensure correct endpoint
    fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Call<ChangePasswordResponse>

    @PUT("api/updatePersonalInfo")  // Ensure correct endpoint
    fun updatePersonalInfo(@Body updatePersonalInfoRequest: UpdatePersonalInfoRequest): Call<UpdatePersonalInfoResponse>

    @Multipart
    @POST("api/uploadProfilePicture") // Adjust as needed
    fun uploadProfilePicture(
        @Part("userId") userId: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<UploadProfilePictureResponse>
}
