package org.wit.quest.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.quest.models.*

class MainApp : Application(), AnkoLogger {

  lateinit var quests: QuestStore
  lateinit var users: UserStore

  override fun onCreate() {
    super.onCreate()
    quests = QuestJSONStore(applicationContext)
    users = UserJSONStore(applicationContext)
    info("Quest App Started")
  }

}