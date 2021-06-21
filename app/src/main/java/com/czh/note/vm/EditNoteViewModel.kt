package com.czh.note.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czh.note.db.AppDatabase
import com.czh.note.db.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNoteViewModel : ViewModel() {

    private val noteDao = AppDatabase.getInstance().noteDao()

    val noteLiveData = MutableLiveData<Note>()
    val saveStatusLiveData = MutableLiveData<Boolean>(false)

    fun getNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = noteDao.getNoteById(id)
            note?.let {
                noteLiveData.postValue(it)
            }
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val rawId = noteDao.addNote(note)
            saveStatusLiveData.postValue(true)
        }
    }
}