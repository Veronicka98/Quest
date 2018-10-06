package org.wit.quest.models

interface QuestStore {
  fun findAll(): List<QuestModel>
  fun create(quest: QuestModel)
  fun update(quest: QuestModel)
}