package com.czh.note.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czh.note.config.AppConfig
import com.czh.note.db.AppDatabase
import com.czh.note.db.Note
import com.czh.note.util.VibratorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteDetailViewModel : ViewModel() {

    private val noteDao = AppDatabase.getInstance().noteDao()

    fun getNote(id: Long): LiveData<Note> {
        return noteDao.getNoteById(id)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val num = noteDao.deleteNote(note)
            if (num == 1) {
                VibratorUtils.shortVibrate(AppConfig.mContext)
            }
        }
    }
}