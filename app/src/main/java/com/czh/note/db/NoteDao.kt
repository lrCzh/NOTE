package com.czh.note.db

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note): Long

    @Delete
    suspend fun deleteNote(note: Note): Int

    @Update
    suspend fun updateNote(note: Note): Int

    @Query("select * from note")
    fun getNotes(): PagingSource<Int, Note>

    @Query("select * from note where title like :keyword or description like :keyword")
    suspend fun getNotesByKeyword(keyword: String): List<Note>

    @Query("select * from note where uid == :id")
    suspend fun getNoteById(id: Long): Note?
}