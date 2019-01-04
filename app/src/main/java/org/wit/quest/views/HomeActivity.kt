package org.wit.quest.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.wit.quest.R
import org.wit.quest.main.MainApp

class HomeActivity : AppCompatActivity(), AnkoLogger , NavigationView.OnNavigationItemSelectedListener{

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    setSupportActionBar(toolbar)

    app = application as MainApp

    // add navigation drawer
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
  }

  override fun onBackPressed() {
    // close drawer when back pressed
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // handle menu item selection
    when (item.itemId) {
      R.id.item_add -> startActivityForResult<QuestActivity>(200)
      R.id.item_list -> startActivityForResult<ListActivity>(200)
      R.id.item_setting -> startActivityForResult<SettingsActivity>(200)
      R.id.item_map -> startActivityForResult<QuestMapsActivity>(200)
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}
