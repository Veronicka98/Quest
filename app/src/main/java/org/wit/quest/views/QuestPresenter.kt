package org.wit.quest.views


import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import org.jetbrains.anko.intentFor
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.Location
import org.wit.quest.models.QuestModel
import org.jetbrains.anko.toast
import org.wit.placemark.helpers.showImagePicker


class QuestPresenter(val activity: QuestActivity)  {

  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  val FULLSCREEN = 3

  lateinit var app : MainApp
  var quest = QuestModel()
  val location = Location(52.245696, -7.139102, 15f) //default location
  var edit = false
  var imgs : ArrayList<ImageView> = ArrayList()
  
  init {
    app = activity.application as MainApp
    if (activity.intent.hasExtra("quest_edit")) {
      edit = true
      quest = activity.intent.extras.getParcelable<QuestModel>("quest_edit")
      activity.showQuest(quest)
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
      activity.toast("Saving Quest")
      activity.setResult(201)
      activity.finish()
    } else {
      // create new quest
      if (quest.name.isNotEmpty() && quest.townland.isNotEmpty() && quest.country.isNotEmpty()) {
        app.users.createQuest(quest.copy())
        activity.toast("Adding Quest")
      } else {
        activity.toast(R.string.toast_promptAdd)
      }
      activity.setResult(200)
      activity.finish()
    }
  }

  fun doCancel() {
    activity.setResult(AppCompatActivity.RESULT_CANCELED)
    activity.finish()
  }

  fun doDelete(quest: QuestModel) {
    app.users.deleteQuest(quest)
    activity.setResult(AppCompatActivity.RESULT_CANCELED)
    activity.finish()
  }

  fun doSelectImage() {
    showImagePicker(activity, IMAGE_REQUEST)
  }

  fun doSetLocation() {
    if (quest.zoom != 0f) {
      location.lat =  quest.lat
      location.lng = quest.lng
      location.zoom = quest.zoom
    }
    activity.startActivityForResult(activity.intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
  }

}