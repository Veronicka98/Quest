package org.wit.quest.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestModel (
    var id: Long = 0,

    var name: String = "",
    var townland: String = "",
    var country: String = "",
    var location: String = "",

    var image: String = "",

    var lat : Double = 0.0,
    var lng: Double = 0.0,

    var Date: String = "",
    var Rating: Int = 1

) : Parcelable
