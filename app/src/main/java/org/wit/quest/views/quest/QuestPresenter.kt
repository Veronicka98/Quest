package org.wit.quest.views.quest


import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import org.jetbrains.anko.intentFor
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.Location
import org.wit.quest.models.QuestModel
import org.jetbrains.anko.toast
import org.wit.placemark.helpers.showImagePicker
import org.wit.quest.views.MapsView


class QuestPresenter(val view: QuestView)  {

  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2

  lateinit var app : MainApp
  var quest = QuestModel()
  val location = Location(52.245696, -7.139102, 15f) //default location
  var edit = false
  var imgs : ArrayList<ImageView> = ArrayList()
  
  init {
    app = view.application as MainApp
    if (view.intent.hasExtra("quest_edit")) {
      edit = true
      quest = view.intent.extras.getParcelable<QuestModel>("quest_edit")
      view.showQuest(quest)
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

}