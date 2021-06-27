package com.czh.note.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val uid: Long = 0,

    @ColumnInfo var title: String,
    @ColumnInfo var date: Long,
    @ColumnInfo var type: Int, // 1、纪念日；2、倒数日（当前只做纪念日）
    @ColumnInfo var description: String? = null,
    @ColumnInfo var location: String? = null,
)