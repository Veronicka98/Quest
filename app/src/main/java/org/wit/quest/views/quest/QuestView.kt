package org.wit.quest.views.quest

import android.content.Intent
import org.jetbrains.anko.intentFor
import org.wit.quest.main.MainApp
import org.wit.quest.models.Location
import org.wit.quest.models.QuestModel
import org.jetbrains.anko.AnkoLogger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_quest.*
import org.wit.placemark.helpers.readImage
import org.wit.placemark.helpers.readImageFromPath
import org.wit.quest.R

class QuestView : AppCompatActivity(), AnkoLogger {

  lateinit var app : MainApp
  lateinit var presenter: QuestPresenter
  var quest = QuestModel()
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  val FULLSCREEN = 3
  var imgs : ArrayList<ImageView> = ArrayList()

  var upQuest : QuestModel = QuestModel()
  var downQuest : QuestModel = QuestModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quest)

    // disable input focus
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    setSupportActionBar(toolbarCreate)

    presenter = QuestPresenter(this)

    app = application as MainApp

    // location button listener
    questLocation.setOnClickListener {presenter.doSetLocation()}

    // choosing images
    chooseImage.setOnClickListener {presenter.doSelectImage()}

    // open new quest edit view if up button is pressed
    imageButtonUp.setOnClickListener{
        startActivityForResult(intentFor<QuestView>().putExtra("quest_edit", upQuest), 201)
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_in_bottom)
        finish()
    }

    // open new quest edit view if down button is pressed
    imageButtonDown.setOnClickListener{
      startActivityForResult(intentFor<QuestView>().putExtra("quest_edit", downQuest), 201)
      overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top)
      finish()
    }

  }

  fun showQuest(quest: QuestModel) {

    app = application as MainApp

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

    questName.setText(quest.name)
    questTownland.setText(quest.townland)
    questCountry.setText(quest.country)
    questDate.setText(quest.date)
    questLatitude.setText(quest.lat.toString())
    questLongtitude.setText(quest.lng.toString())
    questDescription.setText(quest.description)
    questNotes.setText(quest.notes)
    questVisited.isChecked = quest.visited
    questFavourite.isChecked = quest.favourite
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

  // add toolbar menu
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_create, menu)
    if (presenter.edit) menu!!.findItem(R.id.item_delete).setVisible(true)
    return super.onCreateOptionsMenu(menu)
  }

  // handle location and image button clicks
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
                // send image to fullscreen view if clicked
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

      // cancel quest view
      R.id.item_cancel -> {
        presenter.doCancel()
      }

      // delete quest
      R.id.item_delete -> {
        presenter.doDelete(quest)
      }

      // save quest
      R.id.item_save -> {
        presenter.doAddOrSave(questName.text.toString(),
            questTownland.text.toString(),
            questCountry.text.toString(),
            questDate.text.toString(),
            questDescription.text.toString(),
            questNotes.text.toString(),
            questRating.rating,
            questVisited.isChecked,
            questFavourite.isChecked)
      }

    }
    return super.onOptionsItemSelected(item)
  }
}
