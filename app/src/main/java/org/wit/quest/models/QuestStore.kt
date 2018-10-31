package org.wit.quest.models

interface QuestStore {
  fun findAll(): List<QuestModel>
  fun create(quest: QuestModel)
  fun delete(quest: QuestModel)
  fun update(quest: QuestModel)
  fun indexOf(quest: QuestModel): Int
  fun size(): Int
  fun visited(): Int
}