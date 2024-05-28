package dev.lengua.model

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class AppDatabase() : RoomDatabase() {
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            if (instance != null)
                return instance!!
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app-db"
            ).build()
            return instance!!
        }
    }
}