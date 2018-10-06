package org.wit.quest.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
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

class ListActivity : AppCompatActivity(), AnkoLogger, QuestListener {

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_list)

    setSupportActionBar(toolbarList)

    app = application as MainApp

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    loadQuests()
  }

  private fun loadQuests() {
    showQuests ( app.quests.findAll())
  }

  fun showQuests (quests: List<QuestModel>) {
    recyclerView.adapter = QuestAdaptor(quests, this)
    recyclerView.adapter!!.notifyDataSetChanged()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    loadQuests()
    super.onActivityResult(requestCode, resultCode, data)
  }

  override fun onQuestClick(quest: QuestModel) {
    startActivityForResult(intentFor<QuestActivity>().putExtra("quest_edit", quest), 201)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_home -> startActivityForResult<HomeActivity>(200)
      R.id.item_add -> startActivityForResult<QuestActivity>(200)
      R.id.item_setting -> startActivityForResult<SettingsActivity>(200)
    }
    return super.onOptionsItemSelected(item)
  }
}
