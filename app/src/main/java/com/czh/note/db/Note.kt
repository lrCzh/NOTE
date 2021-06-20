package com.czh.note.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val date: String, // 2021/06/18
    @ColumnInfo val description: String,
    @ColumnInfo val weather: String, // 不使用类型来进行记录，是为了数据库用其他工具打开也能直观看到数据，下同
    @ColumnInfo val mood: String
)