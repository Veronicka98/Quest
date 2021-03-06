package org.wit.quest.views.quest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.wit.quest.R
import kotlinx.android.synthetic.main.activity_fullscreen.*
import org.wit.placemark.helpers.*


class FullscreenActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_fullscreen)

    // get image from intent to display
    if (intent.hasExtra("image")) {
      var image = intent.extras.getString("image")
      fullscreenImage.setImageBitmap(readImageFromPath(this, image))
    }
  }
  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }

}

