package org.wit.quest.views

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.quest.main.MainApp
import org.wit.quest.models.QuestModel


class QuestListPresenter(val activity: ListActivity) {
  
  var app: MainApp

  init {
    app = activity.application as MainApp
  }

  fun getQuests() = app.users.findAllQuests()

  fun doAddQuest() {
    activity.startActivityForResult<QuestActivity>(0)
  }

  fun doHomeQuest() {
    activity.startActivityForResult<HomeActivity>(0)
  }

  fun doSettings() {
    activity.startActivityForResult<SettingsActivity>(0)
  }

  fun doShowQuestsMap() {
    activity.startActivity<QuestMapsActivity>()
  }

  fun doEditQuest(quest: QuestModel) {
    activity.startActivityForResult(activity.intentFor<QuestActivity>().putExtra("quest_edit", quest), 0)
  }

}