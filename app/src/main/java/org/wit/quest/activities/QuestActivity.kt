package org.wit.quest.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_quest.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.placemark.helpers.showImagePicker
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.QuestModel
import readImage
import readImageFromPath

class QuestActivity : AppCompatActivity(), AnkoLogger {

  lateinit var app : MainApp
  var quest = QuestModel()
  var edit = false
  val IMAGE_REQUEST = 1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quest)

    setSupportActionBar(toolbarCreate)

    app = application as MainApp

    if (intent.hasExtra("quest_edit")) {
      quest = intent.extras.getParcelable<QuestModel>("quest_edit")

      questName.setText(quest.name)
      questTownland.setText(quest.townland)
      questLocation.setText(quest.location)
      questCountry.setText(quest.country)

      questImage.setImageBitmap(readImageFromPath(this, quest.image))

      if (quest.image != null) {
        chooseImage.setText(R.string.button_changeImage)
      }
      edit = true
    }

    chooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_create, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {

      IMAGE_REQUEST -> {
        if (data != null) {
          quest.image = data.getData().toString()
          questImage.setImageBitmap(readImage(this, resultCode, data))
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
      R.id.item_save -> {
        quest.name = questName.text.toString()
        quest.townland = questTownland.text.toString()
        quest.country = questCountry.text.toString()
        quest.location = questLocation.text.toString()

        if (edit) {
          app.quests.update(quest.copy())
          info("Update Button Pressed")
          setResult(201)
          finish()
        } else {
          if (quest.name.isNotEmpty() && quest.townland.isNotEmpty()
              && quest.country.isNotEmpty() && quest.location.isNotEmpty()) {
            app.quests.create(quest.copy())
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
