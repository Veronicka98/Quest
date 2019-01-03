package org.wit.quest.adaptors

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.card_quest.view.*
import org.wit.quest.R
import org.wit.quest.models.QuestModel
import org.wit.placemark.helpers.*

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

    var imgs : ArrayList<ImageView> = ArrayList()

    fun bind(quest: QuestModel,  listener : QuestListener) {

      // add image views to list
      imgs.add(itemView.imageIcon)
      imgs.add(itemView.imageIcon1)
      imgs.add(itemView.imageIcon2)
      imgs.add(itemView.imageIcon3)
      // hide all views
      itemView.imageIcon1.visibility = View.GONE
      itemView.imageIcon2.visibility = View.GONE
      itemView.imageIcon3.visibility = View.GONE

      // set name and townland
      itemView.questName.text = quest.name
      itemView.questTownland.text = quest.townland
      // set date if visited
      if (quest.date != "")
        itemView.questDate.text = "Visited on " + quest.date

      // set images
      for(i in quest.images) {
        imgs[quest.images.indexOf(i)].visibility = View.VISIBLE
        imgs[quest.images.indexOf(i)].setImageBitmap(readImageFromPath(itemView.context, i))
      }

      itemView.setOnClickListener { listener.onQuestClick(quest) }
    }
  }
}

