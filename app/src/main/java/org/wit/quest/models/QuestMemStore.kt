package org.wit.quest.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
  return lastId++
}

class QuestMemStore : QuestStore, AnkoLogger {
  override fun size(): Int {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun indexOf(quest: QuestModel): Int {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  val quests = ArrayList<QuestModel>()

  override fun findAll(): ArrayList<QuestModel> {
    return quests
  }

  override fun create(quest: QuestModel) {
    quest.id = getId()
    quests.add(quest)
    logAll()
  }

  override fun delete(quest: QuestModel) {
    if (quest != null) {
      quests.remove(quest)
    }
  }

  override fun update(quest: QuestModel) {
    var foundQuest: QuestModel? = quests.find { q -> q.id == quest.id }
    if (foundQuest != null) {
      foundQuest.name = quest.name
      foundQuest.townland = quest.townland
      foundQuest.country = quest.country

      foundQuest.image = quest.image
      foundQuest.image1 = quest.image1
      foundQuest.image2 = quest.image2
      foundQuest.image3 = quest.image3

      foundQuest.date = quest.date

      foundQuest.description = quest.description
      foundQuest.notes = quest.notes
      foundQuest.rating = quest.rating
      foundQuest.visited = quest.visited

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