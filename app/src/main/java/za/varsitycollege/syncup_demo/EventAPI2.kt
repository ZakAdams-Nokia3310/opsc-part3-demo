package za.varsitycollege.syncup_demo.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import za.varsitycollege.syncup_demo.EventData

// Define the API endpoints for event-related requests
interface EventAPI2 {

    // Endpoint to create a new event (POST request)
    @POST("events/create")
    fun createEvent(@Body eventRequest: EventRequest): Call<EventResponse>

    // Endpoint to get the list of upcoming events (GET request)
    @GET("events/upcoming")
    fun getUpcomingEvents(): Call<List<EventData>>

    @POST("/subscribe")
    fun subscribeToEvent(@Body subscriptionRequest: SubscriptionRequest): Call<SubscriptionResponse>

    @GET("/events/subscribed")  // Adjust the path based on your server's API endpoint
    fun getSubscribedEvents(): Call<List<EventData>>

    @GET("events/{eventId}/djs")
    fun getDJDetailsForEvent(@Path("eventId") eventId: String): Call<List<DJDetails>>
}
