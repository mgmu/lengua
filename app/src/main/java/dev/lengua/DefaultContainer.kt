package dev.lengua

import android.content.Context
import dev.lengua.model.AppDatabase
import dev.lengua.model.DefaultEntriesRepository
import dev.lengua.model.EntriesRepository

/**
 * The default dependency container of the application.
 *
 * @property context the context to retrieve the application context
 */
class DefaultContainer(private val context: Context): LenguaAppContainer {

    override val database: AppDatabase by lazy {
        AppDatabase.instance(context)
    }

    override val entriesRepository: EntriesRepository by lazy {
        DefaultEntriesRepository(database.entryDao())
    }
}
