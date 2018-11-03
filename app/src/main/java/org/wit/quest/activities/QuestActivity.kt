package org.wit.quest.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_quest.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.placemark.helpers.showImagePicker
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.Location
import org.wit.quest.models.QuestModel
import org.wit.placemark.helpers.*

class QuestActivity : AppCompatActivity(), AnkoLogger {

  lateinit var app : MainApp
  var quest = QuestModel()
  val location = Location(52.245696, -7.139102, 15f) //default location
  var edit = false
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  val FULLSCREEN = 3
  var imgs : ArrayList<ImageView> = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quest)

    // disable input focus
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    setSupportActionBar(toolbarCreate)

    app = application as MainApp

    // up and down quests
    var upQuest : QuestModel = QuestModel()
    var downQuest : QuestModel = QuestModel()

    // image view placeholders
    imgs.add(questImage)
    imgs.add(questImage1)
    imgs.add(questImage2)
    imgs.add(questImage3)

    // hide all images and buttons
    questImage1.visibility = View.GONE
    questImage2.visibility = View.GONE
    questImage3.visibility = View.GONE
    imageButtonDown.visibility = View.GONE
    imageButtonUp.visibility = View.GONE

    // check for quest to edit
    if (intent.hasExtra("quest_edit")) {
      edit = true
      quest = intent.extras.getParcelable<QuestModel>("quest_edit")

      // set all fields
      questName.setText(quest.name)
      questTownland.setText(quest.townland)
      questCountry.setText(quest.country)
      questDate.setText(quest.date)
      questLatitude.setText(quest.lat.toString())
      questLongtitude.setText(quest.lng.toString())
      questDescription.setText(quest.description)
      questNotes.setText(quest.notes)
      questVisited.isChecked = quest.visited
      questRating.rating = quest.rating

      // set images
      for(i in quest.images) {
        imgs[quest.images.indexOf(i)].visibility = View.VISIBLE
        imgs[quest.images.indexOf(i)].setImageBitmap(readImageFromPath(this, i))
      }

      // change button text
      if (quest.images.size != 0) {
        chooseImage.setText(R.string.button_changeImage)
      }

      // find index of quest in all quests
      var index = app.users.indexOfQuests(quest)

      // show down button if needed
      if (index + 1 < app.users.sizeQuests()) {
        downQuest = app.users.findAllQuests()[index + 1]
        imageButtonDown.visibility = View.VISIBLE
      }

      // show up button if needed
      if (index > 0) {
        upQuest = app.users.findAllQuests()[index - 1]
        imageButtonUp.visibility = View.VISIBLE
      }
    }

    // location button listener
    questLocation.setOnClickListener {
      if (quest.zoom != 0f) {
        location.lat =  questLatitude.text.toString().toDouble()
        location.lng = questLongtitude.text.toString().toDouble()
        location.zoom = quest.zoom
      }
      startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
    }

    // choosing images
    chooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    // open new quest edit activity if up button is pressed
    imageButtonUp.setOnClickListener{
        startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", upQuest), 201)
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom)
        finish()
    }

    // open new quest edit activity if down button is pressed
    imageButtonDown.setOnClickListener{
      startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", downQuest), 201)
      overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top)
      finish()
    }

  }

  // add toolbar menu
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_create, menu)
    if (!edit) menu!!.findItem(R.id.item_delete).setVisible(false)
    return super.onCreateOptionsMenu(menu)
  }

  // hangle location and image button clicks
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {

      IMAGE_REQUEST -> {

        // clear array of images to start over
        quest.images.clear()
        // hide images in case less are selected than visible
        questImage1.visibility = View.GONE
        questImage2.visibility = View.GONE
        questImage3.visibility = View.GONE

        if (data != null) {
          // 1 image selected
          if (data.data != null) {
            quest.images.add(data.getData().toString())
            questImage.setImageBitmap(readImage(this, resultCode, data.getData()))

            // more than 1 image selected
          } else {

            var i = data.clipData.itemCount

            if(i > 0) {
              for (i in 0..i - 1) {
                // set all image views and make them visible
                imgs[i].visibility = View.VISIBLE
                quest.images.add(data.clipData.getItemAt(i).uri.toString())
                imgs[i].setImageBitmap(readImage(this, resultCode, data.clipData.getItemAt(i).uri))
                // send image to fullscreen activity if clicked
                imgs[i].setOnClickListener{
                  startActivityForResult(intentFor<FullscreenActivity>().putExtra("image", data.clipData.getItemAt(i).uri.toString()), FULLSCREEN)
                  overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                }

              }
            }
          }
        }
      }

      LOCATION_REQUEST -> {
        if (data != null) {
          val location = data.extras.getParcelable<Location>("location")
          quest.lat = location.lat
          quest.lng = location.lng
          questLatitude.setText(quest.lat.toString())
          questLongtitude.setText(quest.lng.toString())
          quest.zoom = location.zoom
        }
      }

    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    // hangle toolbar item selection
    when (item?.itemId) {

      // cancel quest activity
      R.id.item_cancel -> {
        setResult(RESULT_CANCELED)
        finish()
      }

      // delete quest
      R.id.item_delete -> {
        app.users.deleteQuest(quest)
        setResult(RESULT_CANCELED)
        finish()
      }

      // save quest
      R.id.item_save -> {

        // get all inputs
        quest.name = questName.text.toString()
        quest.townland = questTownland.text.toString()
        quest.country = questCountry.text.toString()
        quest.date = questDate.text.toString()
        quest.description = questDescription.text.toString()
        quest.notes = questNotes.text.toString()
        quest.rating = questRating.rating
        quest.visited = questVisited.isChecked

        if (edit) {
          // update existing quest
          app.users.updateQuest(quest.copy())
          toast("Saving Quest")
          setResult(201)
          finish()
        } else {
          // create new quest
          if (quest.name.isNotEmpty() && quest.townland.isNotEmpty() && quest.country.isNotEmpty()) {
            app.users.createQuest(quest.copy())
            toast("Adding Quest")
          } else {
            toast(R.string.toast_promptAdd)
          }
          setResult(200)
          finish()
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }
}
