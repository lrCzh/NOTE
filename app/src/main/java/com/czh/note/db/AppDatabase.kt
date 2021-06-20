package com.czh.note.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.czh.note.config.AppConfig

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        private const val DB_NAME = "note.db"

        private var sInstance: AppDatabase? = null

        fun getInstance(dbName: String = DB_NAME): AppDatabase {
            return sInstance ?: synchronized(AppDatabase::class.java) {
                sInstance ?: Room.databaseBuilder(
                    AppConfig.mContext,
                    AppDatabase::class.java,
                    dbName
                ).build().also { sInstance = it }
            }
        }
    }
}