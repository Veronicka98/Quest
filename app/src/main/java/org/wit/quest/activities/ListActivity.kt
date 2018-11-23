package org.wit.quest.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.quest.R
import org.wit.quest.adaptors.QuestAdaptor
import org.wit.quest.adaptors.QuestListener
import org.wit.quest.main.MainApp
import org.wit.quest.models.QuestModel

class ListActivity : AppCompatActivity(), AnkoLogger, QuestListener, NavigationView.OnNavigationItemSelectedListener {

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)

    setSupportActionBar(toolbarList)

    app = application as MainApp

    // add quests to list
    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    loadQuests()

    // add navigation drawer
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbarList, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
  }

  // load quests
  private fun loadQuests() {
    showQuests(app.users.findAllQuests())
  }

  // load quests into adaptor to display as list
  fun showQuests (quests: List<QuestModel>) {
    recyclerView.adapter = QuestAdaptor(quests, this)
    recyclerView.adapter!!.notifyDataSetChanged()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    loadQuests()
    super.onActivityResult(requestCode, resultCode, data)
  }

  // start quest activity with quest clicked on
  override fun onQuestClick(quest: QuestModel) {
    startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", quest), 201)
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
      R.id.item_add -> startActivityForResult<QuestActivity>(200)
      R.id.item_setting -> startActivityForResult<SettingsActivity>(200)
      R.id.item_map -> startActivityForResult<QuestMapsActivity>(200)
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}
