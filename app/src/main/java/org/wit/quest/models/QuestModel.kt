package org.wit.quest.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestModel (
    var id: Long = 0,

    var name: String = "",
    var townland: String = "",
    var country: String = "",

    var image: String = "",

    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,

    var date: String = "",
    var rating: Int = 1

) : Parcelable


@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable
