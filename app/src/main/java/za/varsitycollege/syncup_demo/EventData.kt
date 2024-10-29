package za.varsitycollege.syncup_demo

import android.os.Parcel
import android.os.Parcelable
import za.varsitycollege.syncup_demo.DJDetails

data class EventData(
    val eventName: String,
    val eventTime: String,
    val eventLocation: String,
    val ticketPrice: String,
    val eventGenre: String?, // Optional genre field, nullable
    val djDetails: List<DJDetails> // List of DJs and their times
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",  // Read event name
        parcel.readString() ?: "",  // Read event time
        parcel.readString() ?: "",  // Read event location
        parcel.readString() ?: "",  // Read ticket price
        parcel.readString(),        // Read genre (nullable)
        parcel.createTypedArrayList(DJDetails.CREATOR) ?: emptyList() // Reference DJDetails.CREATOR
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventName)
        parcel.writeString(eventTime)
        parcel.writeString(eventLocation)
        parcel.writeString(ticketPrice)
        parcel.writeString(eventGenre) // Write the genre (nullable)
        parcel.writeTypedList(djDetails) // Write the list of DJs to the Parcel
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventData> {
        override fun createFromParcel(parcel: Parcel): EventData {
            return EventData(parcel)
        }

        override fun newArray(size: Int): Array<EventData?> {
            return arrayOfNulls(size)
        }
    }
}
