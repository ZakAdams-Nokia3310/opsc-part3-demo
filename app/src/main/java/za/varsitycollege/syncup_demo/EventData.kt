package za.varsitycollege.syncup_demo

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class EventData(
    val name: String = "",
    val time: String = "",
    val date: String = "",
    val location: String = "",
    val ticketPrice: String = "",
    val genre: String? = null,
    val djDetails: List<DJDetails> = emptyList()
) : Parcelable {

    // Constructor to create EventData from a Parcel
    private constructor(parcel: Parcel) : this(
        name = parcel.readString().orEmpty(),
        time = parcel.readString().orEmpty(),
        date = parcel.readString().orEmpty(),
        location = parcel.readString().orEmpty(),
        ticketPrice = parcel.readString().orEmpty(),
        genre = parcel.readString(),
        djDetails = parcel.createTypedArrayList(DJDetails.CREATOR) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(time)
        parcel.writeString(date)
        parcel.writeString(location)
        parcel.writeString(ticketPrice)
        parcel.writeString(genre)
        parcel.writeTypedList(djDetails)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<EventData> {
        override fun createFromParcel(parcel: Parcel): EventData = EventData(parcel)
        override fun newArray(size: Int): Array<EventData?> = arrayOfNulls(size)
    }
}
