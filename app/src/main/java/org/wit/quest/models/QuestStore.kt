package org.wit.quest.models

interface QuestStore {
  fun findAll(): List<QuestModel>
  fun findOne(id: Long): QuestModel
  fun create(quest: QuestModel)
  fun update(quest: QuestModel)
  fun getLastId(): Long
}