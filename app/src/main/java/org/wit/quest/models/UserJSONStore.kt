package org.wit.quest.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.quest.helpers.*
import java.util.*

val USER_JSON_FILE = "users.json"
val gsonUserBuilder = GsonBuilder().setPrettyPrinting().create()
val listUserType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type
var loggedIn = UserModel()

fun generateRandomId(): Long {
  return Random().nextLong()
}

class UserJSONStore : UserStore, AnkoLogger {

  val context: Context
  var users = mutableListOf<UserModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, USER_JSON_FILE)) {
      deserialize()
    }
  }

  override fun findAll(): MutableList<UserModel> {
    return users
  }

  override fun create(user: UserModel) {
    user.id = generateRandomId()
    users.add(user)
    serialize()
  }


  override fun update(user: UserModel) {
    var foundUser: UserModel? = users.find { q -> q.id == user.id }
    if (foundUser != null) {
      foundUser.email = user.email
      foundUser.password = user.password
      foundUser.profileImage = user.profileImage
      foundUser.quests = user.quests

      logAll()
      serialize()
    }
  }

  override fun delete(user: UserModel) {
    if (user != null) {
      users.remove(user)
    }
  }

  override fun getLoggedIn(): UserModel {
    return loggedIn
  }

  override fun logOut() {
    loggedIn = UserModel()
  }

  override fun login(user: UserModel): Boolean {
    var foundUser: UserModel? = users.find { u -> u.email == user.email && u.password == user.password }
    if (foundUser != null) {
      loggedIn = foundUser
      logAll()
      return false // login success
    }
    return true // login failed
  }

  override fun signup(user: UserModel): Boolean {
    var foundUser: UserModel? = users.find { u -> u.email == user.email }
    if (foundUser != null) {
      return true // sign up failed
    }
    return false //signup success
  }

  fun logAll() {
    users.forEach { info("${it}") }
  }

  override fun size(): Int {
    return users.size
  }

  override fun indexOf(user: UserModel): Int {
    return users.indexOf(user)
  }


  override fun createQuest(quest: QuestModel) {
    quest.id = generateRandomId()
    loggedIn.quests.add(quest)
    serialize()
  }

  override fun visited(): Int {
    var no_visited = 0
    loggedIn.quests.forEach{
      if (it.visited) no_visited++
    }
    return no_visited
  }

  override fun updateQuest(quest: QuestModel) {
    var foundQuest: QuestModel? = loggedIn.quests.find { q -> q.id == quest.id }
    if (foundQuest != null) {
      foundQuest.name = quest.name
      foundQuest.townland = quest.townland
      foundQuest.country = quest.country

      foundQuest.images = quest.images

      foundQuest.date = quest.date

      foundQuest.description = quest.description
      foundQuest.notes = quest.notes
      foundQuest.rating = quest.rating
      foundQuest.visited = quest.visited

      foundQuest.lat = quest.lat
      foundQuest.lng = quest.lng
      foundQuest.zoom = quest.zoom

      logAllQuests()
      serialize()
    }
  }

  override fun deleteQuest(quest: QuestModel) {
    if (quest != null) {
      loggedIn.quests.remove(quest)
    }
  }

  fun logAllQuests() {
    loggedIn.quests.forEach { info("${it}") }
  }

  override fun sizeQuests(): Int {
    return loggedIn.quests.size
  }

  override fun indexOfQuests(quest: QuestModel): Int {
    return loggedIn.quests.indexOf(quest)
  }

  override fun findAllQuests(): MutableList<QuestModel> {
    return loggedIn.quests
  }

  private fun serialize() {
    val jsonString = gsonUserBuilder.toJson(users, listUserType)
    write(context, USER_JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, USER_JSON_FILE)
    users = Gson().fromJson(jsonString, listUserType)
  }

}