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
    var image1: String = "",
    var image2: String = "",
    var image3: String = "",

    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 5f,

    var date: String = "",

    var description: String = "",
    var notes: String = "",
    var visited: Boolean = false,
    var rating: Float = 1F

) : Parcelable


@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable
