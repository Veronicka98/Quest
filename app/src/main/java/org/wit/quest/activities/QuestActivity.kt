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

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    setSupportActionBar(toolbarCreate)

    app = application as MainApp

    var upQuest : QuestModel = QuestModel()
    var downQuest : QuestModel = QuestModel()
    imgs.add(questImage)
    imgs.add(questImage1)
    imgs.add(questImage2)
    imgs.add(questImage3)

    questImage1.visibility = View.GONE
    questImage2.visibility = View.GONE
    questImage3.visibility = View.GONE
    imageButtonDown.visibility = View.GONE
    imageButtonUp.visibility = View.GONE

    if (intent.hasExtra("quest_edit")) {
      edit = true
      quest = intent.extras.getParcelable<QuestModel>("quest_edit")

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

      for(i in quest.images) {
        imgs[quest.images.indexOf(i)].visibility = View.VISIBLE
        imgs[quest.images.indexOf(i)].setImageBitmap(readImageFromPath(this, i))
      }

      if (quest.images.size != 0) {
        chooseImage.setText(R.string.button_changeImage)
      }

      var index = app.users.indexOfQuests(quest)

      if (index + 1 < app.users.sizeQuests()) {
        downQuest = app.users.findAllQuests()[index + 1]
        imageButtonDown.visibility = View.VISIBLE
      }

      if (index > 0) {
        upQuest = app.users.findAllQuests()[index - 1]
        imageButtonUp.visibility = View.VISIBLE
      }

    }

    questLocation.setOnClickListener {
      if (quest.zoom != 0f) {
        location.lat =  questLatitude.text.toString().toDouble()
        location.lng = questLongtitude.text.toString().toDouble()
        location.zoom = quest.zoom
      }
      startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
    }

    chooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    for(i in quest.images) {
      imgs[quest.images.indexOf(i)].setOnClickListener{
        startActivityForResult(intentFor<FullscreenActivity>().putExtra("image", i), FULLSCREEN)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
      }
    }

    imageButtonUp.setOnClickListener{
        startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", upQuest), 201)
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom)
        finish()

    }

    imageButtonDown.setOnClickListener{
      startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", downQuest), 201)
      overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top)
      finish()
    }

  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_create, menu)
    if (!edit) menu!!.findItem(R.id.item_delete).setVisible(false)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {

      IMAGE_REQUEST -> {

        quest.images.clear()
        questImage1.visibility = View.GONE
        questImage2.visibility = View.GONE
        questImage3.visibility = View.GONE

        if (data != null) {

          if (data.data != null) {
            quest.images.add(data.getData().toString())
            questImage.setImageBitmap(readImage(this, resultCode, data.getData()))
          } else {

            var i = data.clipData.itemCount

            if(i > 0) {
              for (i in 0..i - 1) {
                imgs[i].visibility = View.VISIBLE
                quest.images.add(data.clipData.getItemAt(i).uri.toString())
                imgs[i].setImageBitmap(readImage(this, resultCode, data.clipData.getItemAt(i).uri))

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
    when (item?.itemId) {
      R.id.item_cancel -> {
        setResult(RESULT_CANCELED)
        finish()
      }
      R.id.item_delete -> {
        app.users.deleteQuest(quest)
        setResult(RESULT_CANCELED)
        finish()
      }
      R.id.item_save -> {
        quest.name = questName.text.toString()
        quest.townland = questTownland.text.toString()
        quest.country = questCountry.text.toString()
        quest.date = questDate.text.toString()

        quest.description = questDescription.text.toString()
        quest.notes = questNotes.text.toString()
        quest.rating = questRating.rating
        quest.visited = questVisited.isChecked

        if (edit) {
          app.users.updateQuest(quest.copy())
          info("Update Button Pressed")
          setResult(201)
          finish()
        } else {
          if (quest.name.isNotEmpty() && quest.townland.isNotEmpty()
              && quest.country.isNotEmpty()) {
            app.users.createQuest(quest.copy())
            info("Add Buttom Pressed")
          } else {
            toast (R.string.toast_promptAdd)
          }

          setResult(200)
          finish()
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }
}
