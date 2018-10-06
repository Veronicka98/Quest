package org.wit.quest.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.wit.quest.R
import org.wit.quest.main.MainApp

class SettingsActivity : AppCompatActivity(), AnkoLogger {

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    setSupportActionBar(toolbarSettings)

    app = application as MainApp
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_home -> startActivityForResult<HomeActivity>(200)
      R.id.item_add -> startActivityForResult<QuestActivity>(200)
      R.id.item_list -> startActivityForResult<ListActivity>(200)
     }
    return super.onOptionsItemSelected(item)
  }
}
