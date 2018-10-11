package org.wit.quest.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
  return lastId++
}

class QuestMemStore : QuestStore, AnkoLogger {

  val quests = ArrayList<QuestModel>()

  override fun findAll(): List<QuestModel> {
    return quests
  }

  override fun create(quest: QuestModel) {
    quest.id = getId()
    quests.add(quest)
    logAll()
  }

  override fun update(quest: QuestModel) {
    var foundQuest: QuestModel? = quests.find { q -> q.id == quest.id }
    if (foundQuest != null) {
      foundQuest.name = quest.name
      foundQuest.townland = quest.townland
      foundQuest.country = quest.country
      foundQuest.image = quest.image

      foundQuest.lat = quest.lat
      foundQuest.lng = quest.lng
      foundQuest.zoom = quest.zoom

      logAll()
    }
  }

  fun logAll() {
    quests.forEach { info("${it}") }
  }

}