package org.wit.quest.views.settings

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.quest.helpers.showImagePicker
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.UserModel
import org.wit.placemark.helpers.*
import org.wit.quest.views.login.LoginActivity
import org.wit.quest.views.maps.QuestMapsView
import org.wit.quest.views.home.HomeActivity
import org.wit.quest.views.list.ListView
import org.wit.quest.views.quest.QuestView

class SettingsActivity : AppCompatActivity(), AnkoLogger, NavigationView.OnNavigationItemSelectedListener {

  lateinit var app: MainApp
  var user = UserModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setSupportActionBar(toolbarSettings)

    app = application as MainApp

    // get logged in user
    user = app.users.getLoggedIn()
    // set profile image
    profileImage.setImageBitmap(readImageFromPath(this, user.profileImage))

    // set user details
    if (user != null) {
      username.text = "Username: " + user.email
      password.text = "Password: " + user.password
    }

    // set statistics
    no_quests.text = "Number of quests: " + app.users.sizeQuests()
    quests_visited.text = "Quests Visited: " + app.users.visited()

    // add navigation drawer
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbarSettings, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    // choose profile image
    chooseProfileImage.setOnClickListener {
      showImagePicker(this, 1)
    }

    // delete profile button clicks
    deleteProfile.setOnClickListener {
      app.users.delete(user)
      startActivity(intentFor<LoginActivity>())
      finish()
    }

    // logout button click
    logout.setOnClickListener {
      app.users.logOut()
      startActivity(intentFor<LoginActivity>())
      finish()
    }

    nav_view.setNavigationItemSelectedListener(this)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {

      // handle profile image pick
      1 -> {
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

  // close drawer on back pressed
  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // handle menu item selection
    when (item.itemId) {
      R.id.item_home -> startActivityForResult<HomeActivity>(200)
      R.id.item_add -> startActivityForResult<QuestView>(200)
      R.id.item_list -> startActivityForResult<ListView>(200)
      R.id.item_map -> startActivityForResult<QuestMapsView>(200)
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}
