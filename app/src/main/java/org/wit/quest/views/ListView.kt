package org.wit.quest.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.wit.quest.R
import org.wit.quest.adaptors.QuestAdaptor
import org.wit.quest.adaptors.QuestListener
import org.wit.quest.main.MainApp
import org.wit.quest.models.QuestModel
import java.util.ArrayList

class ListView : AppCompatActivity(), AnkoLogger, QuestListener, NavigationView.OnNavigationItemSelectedListener {

  lateinit var app: MainApp
  val FAV = 1
  lateinit var presenter: QuestListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)

    setSupportActionBar(toolbarList)

    app = application as MainApp

    presenter = QuestListPresenter(this)

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

    if (intent.hasExtra("fav")) {
      var q = presenter.getQuests()
      var favs : ArrayList<QuestModel> = ArrayList()
      q.forEach()
      {
        if (it.favourite == true) favs.add(it)
      }
      showQuests(favs)
    } else {
      showQuests(presenter.getQuests())
    }
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

  // start quest view with quest clicked on
  override fun onQuestClick(quest: QuestModel) {
    presenter.doEditQuest(quest)
  }

  override fun onBackPressed() {
    // close drawer on back pressed
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  // add toolbar menu
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_fav, menu)
    if (intent.hasExtra("fav")) {
      menu!!.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.round_favorite_white_18dp))
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // handle menu item selection
    when (item.itemId) {
      R.id.item_home -> presenter.doHomeQuest()
      R.id.item_add -> presenter.doAddQuest()
      R.id.item_setting -> presenter.doSettings()
      R.id.item_map -> presenter.doShowQuestsMap()
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    // hangle toolbar item selection
    when (item?.itemId) {

      // fav quests
      R.id.item_fav -> {
        var fav = 1
        if (intent.hasExtra("fav")) {
          startActivity(intentFor<ListView>())
          finish()
        } else {
          startActivityForResult(intentFor<ListView>().putExtra("fav", fav), FAV)
          finish()
        }
      }

    }
    return super.onOptionsItemSelected(item)
  }
}
