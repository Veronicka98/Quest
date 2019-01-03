package org.wit.quest.models

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.quest.helpers.*
import java.io.File
import java.io.FileInputStream
import java.util.*

val USER_JSON_FILE = "users.json"
val gsonUserBuilder = GsonBuilder().setPrettyPrinting().create()
val listUserType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type
var loggedIn = UserModel()

fun generateRandomId(): Long {
  return Random().nextLong()
}

class UserJSONStore : UserStore, AnkoLogger {

  // ---------------------------- user functions -------------------------------

  val context: Context
  var users = mutableListOf<UserModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, USER_JSON_FILE)) {
      deserialize()
    }
  }

  // get all users
  override fun findAll(): MutableList<UserModel> {
    return users
  }

  // create new user
  override fun create(user: UserModel) {
    user.id = generateRandomId()

    var q1 = QuestModel(0, "Dún Aonghasa", "Kilmurvy, Galway", "Ireland", ArrayList(), 53.1259, -9.766364, 15f, "", "", "", false, false, 1F)
    var q2 = QuestModel(0, "Mullaghnashee", "Fairymount Hill, Roscommon", "Ireland", ArrayList(), 53.841915,  -8.487268, 15f, "", "","", false,false,  1F)
    var q3 = QuestModel(0, "Keeston Castle", "Pembrokeshire", "Britain", ArrayList(),  51.835147 , -5.051843, 15f, "", "","", false, false, 1F)
    q1.id = generateRandomId()
    q2.id = generateRandomId()
    q3.id = generateRandomId()
    user.quests.add(q1)
    user.quests.add(q2)
    user.quests.add(q3)

    users.add(user)
    serialize()
  }

  // update new user
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

  // delete user
  override fun delete(user: UserModel) {
    if (user != null) {
      users.remove(user)
    }
  }

  // get logged in user
  override fun getLoggedIn(): UserModel {
    return loggedIn
  }

  // log out
  override fun logOut() {
    loggedIn = UserModel()
  }

  // login
  override fun login(user: UserModel): Boolean {
    var foundUser: UserModel? = users.find { u -> u.email == user.email && u.password == user.password }
    if (foundUser != null) {
      loggedIn = foundUser
      logAll()
      return false // login success
    }
    return true // login failed
  }

  // sign up
  override fun signup(user: UserModel): Boolean {
    var foundUser: UserModel? = users.find { u -> u.email == user.email }
    if (foundUser != null) {
      return true // sign up failed
    }
    return false //signup success
  }

  // print all users to log
  fun logAll() {
    users.forEach { info("${it}") }
  }

  // amount of users
  override fun size(): Int {
    return users.size
  }

  // index of user
  override fun indexOf(user: UserModel): Int {
    return users.indexOf(user)
  }

  // ---------------------------- quest functions --------------------------------

  // create new quest
  override fun createQuest(quest: QuestModel) {
    quest.id = generateRandomId()
    loggedIn.quests.add(quest)
    serialize()
  }

  // amount of visited quests
  override fun visited(): Int {
    var no_visited = 0
    loggedIn.quests.forEach{
      if (it.visited) no_visited++
    }
    return no_visited
  }

  // update existing quest
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
      foundQuest.favourite = quest.favourite
      foundQuest.lat = quest.lat
      foundQuest.lng = quest.lng
      foundQuest.zoom = quest.zoom

      logAllQuests()
      serialize()
    }
  }

  // delete quest
  override fun deleteQuest(quest: QuestModel) {
    if (quest != null) {
      loggedIn.quests.remove(quest)
    }
  }

  // log all quests
  fun logAllQuests() {
    loggedIn.quests.forEach { info("${it}") }
  }

  // size of quests
  override fun sizeQuests(): Int {
    return loggedIn.quests.size
  }

  // index of queSts
  override fun indexOfQuests(quest: QuestModel): Int {
    return loggedIn.quests.indexOf(quest)
  }

  // get all quests
  override fun findAllQuests(): MutableList<QuestModel> {
    return loggedIn.quests
  }

  // put all users into file
  private fun serialize() {
    val jsonString = gsonUserBuilder.toJson(users, listUserType)
    write(context, USER_JSON_FILE, jsonString)
  }

  // get all users from file
  private fun deserialize() {
    val jsonString = read(context, USER_JSON_FILE)
    users = Gson().fromJson(jsonString, listUserType)
  }

}