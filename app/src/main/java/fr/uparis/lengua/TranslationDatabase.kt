package fr.uparis.lengua

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Dictionary::class, Word::class], version = 1)
abstract class TranslationDatabase: RoomDatabase() {
    abstract fun iDao(): IDao

    companion object {
        @Volatile
        private var instance: TranslationDatabase? = null

        fun getDatabase(context: Context): TranslationDatabase {
            if (instance != null)
                return instance!!
            instance = Room
                .databaseBuilder(
                    context.applicationContext,
                    TranslationDatabase::class.java,
                    "translation")
                .fallbackToDestructiveMigration()
                .build()
            return instance!!
        }
    }
}