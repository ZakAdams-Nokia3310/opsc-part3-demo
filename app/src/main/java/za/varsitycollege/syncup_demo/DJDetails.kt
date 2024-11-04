package za.varsitycollege.syncup_demo

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DJDetails(
    val name: String = "",
    val genre: String = "",
    val time: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        name = parcel.readString().orEmpty(),
        genre = parcel.readString().orEmpty(),
        time = parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(genre)
        parcel.writeString(time)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DJDetails> {
        override fun createFromParcel(parcel: Parcel): DJDetails = DJDetails(parcel)
        override fun newArray(size: Int): Array<DJDetails?> = arrayOfNulls(size)
    }
}
