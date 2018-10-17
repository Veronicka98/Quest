package org.wit.quest.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.quest.models.QuestJSONStore
import org.wit.quest.models.QuestMemStore
import org.wit.quest.models.QuestModel
import org.wit.quest.models.QuestStore

class MainApp : Application(), AnkoLogger {

  lateinit var quests: QuestStore

  override fun onCreate() {
    super.onCreate()
    quests = QuestJSONStore(applicationContext)
    info("Quest App Started")
  }

}