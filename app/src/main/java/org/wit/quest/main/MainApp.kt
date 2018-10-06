package org.wit.quest.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.quest.models.QuestMemStore

class MainApp : Application(), AnkoLogger {

  val quests = QuestMemStore()

  override fun onCreate() {
    super.onCreate()
    info("Quest App Started")
  }
}