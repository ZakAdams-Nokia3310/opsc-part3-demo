package za.varsitycollege.syncup_demo.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import za.varsitycollege.syncup_demo.EventData

interface EventAPI2 {

    @POST("api/events/create")
    fun createEvent(
        @Header("Authorization") token: String,
        @Body eventRequest: EventRequest
    ): Call<EventResponse>

    @GET("api/events/upcoming")
    fun getUpcomingEvents(@Header("Authorization") token: String): Call<List<EventData>>

    @POST("api/events/subscribe")
    fun subscribeToEvent(
        @Body subscriptionRequest: SubscriptionRequest,
        @Header("Authorization") token: String
    ): Call<SubscriptionResponse>

    @GET("api/events/subscribed")
    fun getSubscribedEvents(
        @Header("Authorization") token: String
    ): Call<List<EventData>>

    @GET("api/events/{eventId}/djs")
    fun getDJDetailsForEvent(
        @Path("eventId") eventId: String,
        @Header("Authorization") token: String
    ): Call<List<DJDetails>>
}
