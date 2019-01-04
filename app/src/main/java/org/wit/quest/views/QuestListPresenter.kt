package org.wit.quest.views

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.quest.main.MainApp
import org.wit.quest.models.QuestModel


class QuestListPresenter(val view: ListView) {
  
  var app: MainApp

  init {
    app = view.application as MainApp
  }

  fun getQuests() = app.users.findAllQuests()

  fun doAddQuest() {
    view.startActivityForResult<QuestView>(0)
  }

  fun doHomeQuest() {
    view.startActivityForResult<HomeActivity>(0)
  }

  fun doSettings() {
    view.startActivityForResult<SettingsActivity>(0)
  }

  fun doShowQuestsMap() {
    view.startActivity<QuestMapsActivity>()
  }

  fun doEditQuest(quest: QuestModel) {
    view.startActivityForResult(view.intentFor<QuestView>().putExtra("quest_edit", quest), 0)
  }

}