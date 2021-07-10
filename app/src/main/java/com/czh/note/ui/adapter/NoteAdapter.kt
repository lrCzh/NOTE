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
import com.czh.note.util.TimeConstants
import com.czh.note.util.TimeUtils

class NoteAdapter(
    diffCallback: DiffUtil.ItemCallback<Note>,
    private val onClick: (note: Note) -> Unit,
    private val onLongClick: (note: Note) -> Unit
) : PagingDataAdapter<Note, NoteAdapter.NoteViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        getItem(position)?.let { note ->
            holder.tvDay.text = "${
                TimeUtils.getTimeSpan(
                    System.currentTimeMillis(),
                    note.date,
                    TimeConstants.DAY
                )
            } "
            holder.tvTitle.text = note.title
            holder.itemView.apply {
                setOnClickListener { onClick.invoke(note) }
                setOnLongClickListener {
                    onLongClick.invoke(note)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = itemView.findViewById(R.id.tv_day)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    }
}
