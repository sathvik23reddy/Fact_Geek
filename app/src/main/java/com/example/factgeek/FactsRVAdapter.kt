package com.example.factgeek

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView

class FactsRVAdapter(private val context: Context, private val listener: IFactsRVAdapter) : RecyclerView.Adapter<FactsRVAdapter.FactViewHolder>() {

    private val allFacts = ArrayList<Fact>()
    inner class FactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.text)
        val delButton: ImageView = itemView.findViewById(R.id.deleteButton)
        val copyButton: ImageView = itemView.findViewById(R.id.copyButton)
        val shareButton: ImageView = itemView.findViewById(R.id.shareButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val viewHolder = FactViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false))
        viewHolder.delButton.setOnClickListener{
            listener.onItemClicked(allFacts[viewHolder.adapterPosition])
        }
        viewHolder.copyButton.setOnClickListener{
            listener.onSaveClicked(allFacts[viewHolder.adapterPosition])
        }
        viewHolder.shareButton.setOnClickListener{
            listener.onShareClicked(allFacts[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        val currentFact = allFacts[position]
        holder.textView.text = currentFact.fact //I Doubt this bro
    }

    override fun getItemCount(): Int {
        return allFacts.size
    }

    fun updateList(newList:List<Fact>){
        allFacts.clear()
        allFacts.addAll(newList)
        notifyDataSetChanged()
    }
}

interface IFactsRVAdapter {
    fun onItemClicked(fact: Fact)
    fun onSaveClicked(fact: Fact)
    fun onShareClicked(fact: Fact) {

    }
}