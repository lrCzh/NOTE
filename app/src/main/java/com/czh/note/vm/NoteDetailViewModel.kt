package com.czh.note.vm

import androidx.lifecycle.MutableLiveData
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

    val noteLiveData = MutableLiveData<Note>()
    val deleteStatusLiveData = MutableLiveData<Boolean>(false)

    fun getNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = noteDao.getNoteById(id)
            if (note == null) {
                deleteStatusLiveData.postValue(true)
            } else {
                noteLiveData.postValue(note!!)
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val num = noteDao.deleteNote(note)
            if (num == 1) {
                deleteStatusLiveData.postValue(true)
                VibratorUtils.shortVibrate(AppConfig.mContext)
            }
        }
    }
}