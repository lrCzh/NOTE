package com.czh.note.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.czh.note.db.Note

object NoteComparator : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.description == newItem.description &&
                oldItem.date == newItem.date &&
                oldItem.location == newItem.location &&
                oldItem.type == newItem.type
    }
}