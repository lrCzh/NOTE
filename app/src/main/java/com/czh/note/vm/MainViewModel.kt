package com.czh.note.vm

import android.util.Log
import androidx.lifecycle.*
import com.czh.note.config.AppConfig
import com.czh.note.db.AppDatabase
import com.czh.note.db.Note
import com.czh.note.util.VibratorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"

    private val noteDao = AppDatabase.getInstance().noteDao()

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val rawId = noteDao.addNote(note)
            Log.d(TAG, rawId.toString())
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val num = noteDao.deleteNote(note)
            Log.d(TAG, num.toString())
            VibratorUtils.shortVibrate(AppConfig.mContext)
        }
    }
}