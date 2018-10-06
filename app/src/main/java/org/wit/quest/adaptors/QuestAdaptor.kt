package org.wit.quest.adaptors

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_quest.view.*
import org.wit.quest.R
import org.wit.quest.models.QuestModel

interface QuestListener {
  fun onQuestClick(quest: QuestModel)
}

class QuestAdaptor constructor(private var quests: List<QuestModel>,
                                   private val listener: QuestListener) : RecyclerView.Adapter<QuestAdaptor.MainHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
    return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_quest, parent, false))
  }

  override fun onBindViewHolder(holder: MainHolder, position: Int) {
    val quest = quests[holder.adapterPosition]
    holder.bind(quest, listener)
  }

  override fun getItemCount(): Int = quests.size

  class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(quest: QuestModel,  listener : QuestListener) {
      itemView.questName.text = quest.name
      itemView.questTownland.text = quest.townland
      itemView.questCountry.text = quest.country
      itemView.questLocation.text = quest.location

      itemView.setOnClickListener { listener.onQuestClick(quest) }
    }
  }
}
