package org.wit.quest.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import org.wit.quest.R

import kotlinx.android.synthetic.main.activity_quest_maps.*
import org.jetbrains.anko.startActivityForResult

class QuestMapsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quest_maps)
    setSupportActionBar(toolbarMap)

    // add navigation drawer
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbarMap, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
  }


  override fun onBackPressed() {
    // close drawer on back pressed
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
      R.id.item_list -> startActivityForResult<ListActivity>(200)
      R.id.item_add -> startActivityForResult<QuestActivity>(200)
      R.id.item_setting -> startActivityForResult<SettingsActivity>(200)
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}
