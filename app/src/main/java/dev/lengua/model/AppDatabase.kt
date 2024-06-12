package dev.lengua.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntryEntity::class], version = 1)
/**
 * The application database.
 */
abstract class AppDatabase: RoomDatabase() {
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        /**
         * Creates if needed and returns the instance of the application
         * database.
         *
         * @param context the context to retrieve to the application context
         * @return the unique instance of the application database.
         */
        fun instance(context: Context): AppDatabase {
            if (instance != null)
                return instance!!
            instance = Room
                .databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app-db"
                )
                .fallbackToDestructiveMigration()
                .build()
            return instance!!
        }
    }
}
