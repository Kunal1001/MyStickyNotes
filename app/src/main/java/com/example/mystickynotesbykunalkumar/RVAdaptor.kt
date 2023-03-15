package com.example.mystickynotesbykunalkumar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RVAdaptor(options: FirestoreRecyclerOptions<Notes>):FirestoreRecyclerAdapter<Notes,RVAdaptor.RVViewHolder>(
    options
) {
    class RVViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val noteText: TextView = itemView.findViewById(R.id.textView)
    }

    fun deleteNote(position: Int){
        snapshots.getSnapshot(position).reference.delete()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        return RVViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false))
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int, model: Notes) {
        holder.noteText.text = model.text
    }
}