package za.varsitycollege.syncup_demo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import za.varsitycollege.syncup_demo.network.AuthService
import za.varsitycollege.syncup_demo.network.EventAPI2

object RetrofitClient {
    private const val BASE_URL = "https://api-twjzcamdda-uc.a.run.app/"  // Replace with actual deployed API URL
    private var retrofit: Retrofit? = null

    // Helper function to get the Retrofit instance
    private fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    // Function to get AuthService instance
    fun getAuthService(): AuthService {
        return getRetrofitInstance().create(AuthService::class.java)
    }

    // Function to get EventService instance
    fun getEventService(): EventAPI2 {
        return getRetrofitInstance().create(EventAPI2::class.java)
    }
}
