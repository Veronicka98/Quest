package org.wit.quest.views.quest


import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.intentFor
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.Location
import org.wit.quest.models.QuestModel
import org.jetbrains.anko.toast
import org.wit.quest.helpers.*
import org.wit.quest.views.MapsView


class QuestPresenter(val view: QuestView)  {

  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2

  var map: GoogleMap? = null

  var app : MainApp
  var quest = QuestModel()
  val location = Location(52.245696, -7.139102, 15f) //default location
  var edit = false
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

  
  init {
    app = view.application as MainApp

    if (view.intent.hasExtra("quest_edit")) {
      edit = true
      quest = view.intent.extras.getParcelable<QuestModel>("quest_edit")
      view.showQuest(quest)
    } else {
      if (checkLocationPermissions(view)) {
        doSetCurrentLocation()
      } else {
        quest.lat = 52.245696
        quest.lng = -7.139102
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    locationService.lastLocation.addOnSuccessListener {
      locationUpdate(it.latitude, it.longitude)
    }
  }

  fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
      doSetCurrentLocation()
    } else {
      locationUpdate(52.245696, -7.139102)
    }
  }

  fun doAddOrSave(questName: String,
                  questTownland: String,
                  questCountry: String,
                  questDate: String,
                  questDescription: String,
                  questNotes: String,
                  questRating: Float,
                  questVisited: Boolean,
                  questFavourite: Boolean) {

    // get all inputs
    quest.name = questName
    quest.townland = questTownland
    quest.country = questCountry
    quest.date = questDate
    quest.description = questDescription
    quest.notes = questNotes
    quest.rating = questRating
    quest.visited = questVisited
    quest.favourite = questFavourite

    if (edit) {
      // update existing quest
      app.users.updateQuest(quest.copy())
      view.toast("Saving Quest")
      view.setResult(201)
      view.finish()
    } else {
      // create new quest
      if (quest.name.isNotEmpty() && quest.townland.isNotEmpty() && quest.country.isNotEmpty()) {
        app.users.createQuest(quest.copy())
        view.toast("Adding Quest")
      } else {
        view.toast(R.string.toast_promptAdd)
      }
      view.setResult(200)
      view.finish()
    }
  }

  fun doCancel() {
    view.setResult(AppCompatActivity.RESULT_CANCELED)
    view.finish()
  }

  fun doDelete(quest: QuestModel) {
    app.users.deleteQuest(quest)
    view.setResult(AppCompatActivity.RESULT_CANCELED)
    view.finish()
  }

  fun doSelectImage() {
    showImagePicker(view, IMAGE_REQUEST)
  }

  fun doSetLocation() {
    if (quest.zoom != 0f) {
      location.lat =  quest.lat
      location.lng = quest.lng
      location.zoom = quest.zoom
    }
    view.startActivityForResult(view.intentFor<MapsView>().putExtra("location", location), LOCATION_REQUEST)
  }

  fun doConfigureMap(m: GoogleMap) {
    map = m
    locationUpdate(quest.lat, quest.lng)
  }

  fun locationUpdate(lat: Double, lng: Double) {
    quest.lat = lat
    quest.lng = lng
    quest.zoom = 15f
    map?.clear()
    map?.uiSettings?.setZoomControlsEnabled(true)
    val options = MarkerOptions().title(quest.name).position(LatLng(quest.lat, quest.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(quest.lat, quest.lng), quest.zoom))
    view?.showQuest(quest)
  }
  
}