package za.varsitycollege.syncup_demo

import android.os.Parcel
import android.os.Parcelable

data class DJDetails(
    val name: String,
    val time: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DJDetails> {
        override fun createFromParcel(parcel: Parcel): DJDetails {
            return DJDetails(parcel)
        }

        override fun newArray(size: Int): Array<DJDetails?> {
            return arrayOfNulls(size)
        }
    }
}
