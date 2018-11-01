package org.wit.quest.models

interface UserStore {
  fun findAll(): List<UserModel>
  fun create(user: UserModel)
  fun delete(user: UserModel)
  fun update(user: UserModel)
  fun indexOf(user: UserModel): Int
  fun size(): Int
  fun login(user: UserModel): Boolean
  fun signup(user: UserModel): Boolean
  fun getLoggedIn() : UserModel
  fun logOut()

  fun createQuest(quest: QuestModel)
  fun visited() : Int
  fun updateQuest(quest: QuestModel)
  fun deleteQuest(quest: QuestModel)
  fun sizeQuests() : Int
  fun indexOfQuests(quest: QuestModel): Int
  fun findAllQuests(): MutableList<QuestModel>
}