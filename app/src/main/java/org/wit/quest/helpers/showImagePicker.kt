package org.wit.placemark.helpers

import android.app.Activity
import android.content.Intent
import org.wit.quest.R

fun showImagePicker(parent: Activity, id: Int) {
  val intent = Intent()
  intent.type = "image/*"
  intent.action = Intent.ACTION_OPEN_DOCUMENT
  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
  intent.addCategory(Intent.CATEGORY_OPENABLE)
  val chooser = Intent.createChooser(intent, R.string.select_quest_image.toString())
  parent.startActivityForResult(chooser, id)
}