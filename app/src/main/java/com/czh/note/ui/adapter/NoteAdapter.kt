package com.czh.note.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.czh.note.R
import com.czh.note.db.Note

class NoteAdapter(
    diffCallback: DiffUtil.ItemCallback<Note>,
    private val callback: (note: Note) -> Unit
) : PagingDataAdapter<Note, NoteAdapter.NoteViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        getItem(position)?.let { note ->
            val date = note.date.split("/")
            holder.tvMonth.text = date[1]
            holder.tvDay.text = date[2]
            holder.tvTitle.text = note.title
            holder.tvDescription.text = note.description
            holder.itemView.setOnClickListener {
                callback.invoke(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMonth: TextView = itemView.findViewById(R.id.tv_month)
        val tvDay: TextView = itemView.findViewById(R.id.tv_day)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
    }
}