package org.wit.quest.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.quest.models.QuestMemStore
import org.wit.quest.models.QuestModel

class MainApp : Application(), AnkoLogger {

  val quests = QuestMemStore()

  override fun onCreate() {
    super.onCreate()
    info("Quest App Started")
    quests.create(QuestModel(0, "Dún Aonghasa", "Kilmurvy, Galway", "Ireland", "", 53.1259, -9.766364, 15f, "", 0))
    quests.create(QuestModel(0, "Mullaghnashee", "Fairymount Hill, Roscommon", "Ireland", "", 53.841915,  -8.487268, 15f, "", 0))
    quests.create(QuestModel(0, "Keeston Castle", "Pembrokeshire", "Britain", "",  51.835147 , -5.051843, 15f, "", 0))
  }
}