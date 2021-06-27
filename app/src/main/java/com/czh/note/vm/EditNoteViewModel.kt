package com.czh.note.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czh.note.db.AppDatabase
import com.czh.note.db.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNoteViewModel : ViewModel() {

    private val noteDao = AppDatabase.getInstance().noteDao()

    val saveStatusLiveData = MutableLiveData<Boolean>(false)

    fun getNote(id: Long): LiveData<Note> {
        return noteDao.getNoteById(id)
    }

    fun saveNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val rawId = noteDao.addNote(note)
            saveStatusLiveData.postValue(true)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val num = noteDao.updateNote(note)
            saveStatusLiveData.postValue(true)
        }
    }
}