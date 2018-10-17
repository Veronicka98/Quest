package org.wit.quest.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.quest.helpers.*
import java.util.*

val JSON_FILE = "quests.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<QuestModel>>() {}.type

fun generateRandomId(): Long {
  return Random().nextLong()
}

class QuestJSONStore : QuestStore, AnkoLogger {

  val context: Context
  var quests = mutableListOf<QuestModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, JSON_FILE)) {
      deserialize()
    }
  }

  override fun findAll(): MutableList<QuestModel> {
    return quests
  }

  override fun create(quest: QuestModel) {
    quest.id = generateRandomId()
    quests.add(quest)
    serialize()
  }


  override fun update(quest: QuestModel) {
    // todo
  }

  override fun delete(quest: QuestModel) {
    // todo
  }

  override fun size(): Int {
    return quests.size
  }

  override fun indexOf(quest: QuestModel): Int {
    return quests.indexOf(quest)
  }

  private fun serialize() {
    val jsonString = gsonBuilder.toJson(quests, listType)
    write(context, JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, JSON_FILE)
    quests = Gson().fromJson(jsonString, listType)
  }
}