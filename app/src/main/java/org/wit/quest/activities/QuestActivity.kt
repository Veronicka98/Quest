package org.wit.quest.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
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
import readImage
import readImageFromPath


class QuestActivity : AppCompatActivity(), AnkoLogger {

  lateinit var app : MainApp
  var quest = QuestModel()
  val location = Location(52.245696, -7.139102, 15f) //default location
  var edit = false
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  val FULLSCREEN = 3

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quest)

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    setSupportActionBar(toolbarCreate)

    app = application as MainApp

    var upQuest : QuestModel = QuestModel()
    var downQuest : QuestModel = QuestModel()


    if (intent.hasExtra("quest_edit")) {
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

      questImage.setImageBitmap(readImageFromPath(this, quest.image))

      if (quest.image1 == "") questImage1.visibility = View.GONE
      else questImage1.setImageBitmap(readImageFromPath(this, quest.image1))

      if (quest.image2 == "") questImage2.visibility = View.GONE
      else questImage2.setImageBitmap(readImageFromPath(this, quest.image2))

      if (quest.image3 == "") questImage3.visibility = View.GONE
      else questImage3.setImageBitmap(readImageFromPath(this, quest.image3))


      if (quest.image != null) {
        chooseImage.setText(R.string.button_changeImage)
      }

      edit = true

      var index = app.quests.indexOf(quest)

      if (index + 1 < app.quests.size()) {
        downQuest = app.quests.findAll()[index + 1]
      } else {
        imageButtonDown.visibility = View.GONE
      }

      if (index > 0) {
        upQuest = app.quests.findAll()[index - 1]
      } else {
        imageButtonUp.visibility = View.GONE
      }

    } else {
      imageButtonDown.visibility = View.GONE
      imageButtonUp.visibility = View.GONE
      questImage1.visibility = View.GONE
      questImage2.visibility = View.GONE
      questImage3.visibility = View.GONE
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

    questImage.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        startActivityForResult(intentFor<FullscreenActivity>().putExtra("image", quest.image), FULLSCREEN)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
      }
    })

    questImage1.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        startActivityForResult(intentFor<FullscreenActivity>().putExtra("image", quest.image1), FULLSCREEN)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
      }
    })

    questImage2.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        startActivityForResult(intentFor<FullscreenActivity>().putExtra("image", quest.image2), FULLSCREEN)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
      }
    })

    questImage3.setOnClickListener(object : View.OnClickListener {
      override fun onClick(v: View?) {
        startActivityForResult(intentFor<FullscreenActivity>().putExtra("image", quest.image3), FULLSCREEN)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
      }
    })

    imageButtonUp.setOnClickListener{
        startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", upQuest), 201)
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom)
        setResult(RESULT_CANCELED)
        finish()

    }

    imageButtonDown.setOnClickListener{
      startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", downQuest), 201)
      overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top)
      setResult(RESULT_CANCELED)
      finish()
    }

  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_create, menu)
    if (!edit) {
      menu!!.findItem(R.id.item_delete).setVisible(false)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {

      IMAGE_REQUEST -> {
        info("Image data: " + data)
        questImage1.visibility = View.GONE
        quest.image1 = ""
        questImage2.visibility = View.GONE
        quest.image2 = ""
        questImage3.visibility = View.GONE
        quest.image3 = ""
        if (data != null) {
          if (data.data != null) {
            quest.image = data.getData().toString()
            questImage.setImageBitmap(readImage(this, resultCode, data.getData()))
          } else {
            info("Image data: data.clipdata")
            var i = data.clipData.itemCount

            if(i > 0) {
              info("Image data: " + i)
              for (i in 0..i - 1) {
                info("Image data: " + data.clipData.getItemAt(i).uri.toString())
                if (i == 0) {
                  quest.image = data.clipData.getItemAt(i).uri.toString()
                  questImage.setImageBitmap(readImage(this, resultCode, data.clipData.getItemAt(i).uri))
                }
                if (i == 1) {
                  quest.image1 = data.clipData.getItemAt(i).uri.toString()
                  questImage1.setImageBitmap(readImage(this, resultCode, data.clipData.getItemAt(i).uri))
                  questImage1.visibility = View.VISIBLE
                }
                if (i == 2) {
                  quest.image2 = data.clipData.getItemAt(i).uri.toString()
                  questImage2.setImageBitmap(readImage(this, resultCode, data.clipData.getItemAt(i).uri))
                  questImage2.visibility = View.VISIBLE
                }
                if (i == 3) {
                  quest.image3 = data.clipData.getItemAt(i).uri.toString()
                  questImage3.setImageBitmap(readImage(this, resultCode, data.clipData.getItemAt(i).uri))
                  questImage3.visibility = View.VISIBLE
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
    when (item?.itemId) {
      R.id.item_cancel -> {
        setResult(RESULT_CANCELED)
        finish()
      }
      R.id.item_delete -> {
        app.quests.delete(quest)
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
          app.quests.update(quest.copy())
          info("Update Button Pressed")
          setResult(201)
          finish()
        } else {
          if (quest.name.isNotEmpty() && quest.townland.isNotEmpty()
              && quest.country.isNotEmpty()) {
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
