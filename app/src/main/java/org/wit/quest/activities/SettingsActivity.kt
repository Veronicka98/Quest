package org.wit.quest.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.placemark.helpers.showImagePicker
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.UserModel
import readImage
import readImageFromPath

class SettingsActivity : AppCompatActivity(), AnkoLogger, NavigationView.OnNavigationItemSelectedListener {

  lateinit var app: MainApp
  var user = UserModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setSupportActionBar(toolbarSettings)

    app = application as MainApp

    user = app.users.getLoggedIn()
    profileImage.setImageBitmap(readImageFromPath(this, user.profileImage))

    if (user != null) {
      username.text = "Username: " + user.email
      password.text = "Password: " + user.password
    }

    no_quests.text = "Number of quests: " + app.quests.size()
    quests_visited.text = "Quests Visited: " + app.quests.visited()

    val toggle = ActionBarDrawerToggle(
        this, drawer_layout, toolbarSettings, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    chooseProfileImage.setOnClickListener {
      showImagePicker(this, 1)
    }

    deleteProfile.setOnClickListener {
      app.users.delete(user)

      startActivity(intentFor<LoginActivity>())
    }

    nav_view.setNavigationItemSelectedListener(this)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {

      1 -> {
        info("Image data: " + data)
        if (data != null) {
          if (data.data != null) {
            user.profileImage = data.getData().toString()
            profileImage.setImageBitmap(readImage(this, resultCode, data.getData()))
            app.users.update(user.copy())
          }
        }
      }
    }
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.item_home -> startActivityForResult<HomeActivity>(200)
      R.id.item_add -> startActivityForResult<QuestActivity>(200)
      R.id.item_list -> startActivityForResult<ListActivity>(200)
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}
