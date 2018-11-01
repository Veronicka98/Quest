package org.wit.placemark.helpers

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.IOException

fun readImage(activity: Activity, resultCode: Int, data: Uri?): Bitmap? {
  var bitmap: Bitmap? = null
  if (resultCode == Activity.RESULT_OK && data != null && data != null) {
    try {
      bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, data)
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
  return bitmap
}