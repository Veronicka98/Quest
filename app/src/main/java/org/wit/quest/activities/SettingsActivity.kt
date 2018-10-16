package org.wit.quest.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.wit.quest.R
import org.wit.quest.main.MainApp

class SettingsActivity : AppCompatActivity(), AnkoLogger, NavigationView.OnNavigationItemSelectedListener {

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setSupportActionBar(toolbarSettings)

    app = application as MainApp

    val toggle = ActionBarDrawerToggle(
        this, drawer_layout, toolbarSettings, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
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
