package dev.lengua

import dev.lengua.model.AppDatabase
import dev.lengua.model.EntriesRepository

/**
 * The application dependencies container.
 */
interface LenguaAppContainer {

    /**
     * The reference to the application database.
     */
    val database: AppDatabase

    /**
     * The reference to the entries repository.
     */
    val entriesRepository: EntriesRepository
}
