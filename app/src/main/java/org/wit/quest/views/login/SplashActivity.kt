package org.wit.quest.views.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import org.wit.quest.R

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    // show splash activity for 5 seconds
    Handler().postDelayed({
      val intent = Intent(this, LoginView::class.java)
      startActivity(intent)
    }, 5000)
  }

}
