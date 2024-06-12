package dev.lengua.model

import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.Flow

/**
 * Interface to the repository of entries.
 */
interface EntriesRepository {

    /**
     * The entry to edit, or null if no entry is selected for edition.
     */
    var entryToEdit: IdentifiedEntry?

    /**
     * The flow of all the entries stored in this repository.
     */
    val allEntries: Flow<List<IdentifiedEntry>>

    /**
     * Adds [entry] to this repository.
     *
     * @param entry the entry to add to this repository.
     */
    suspend fun add(entry: Entry)

    /**
     * Deletes [entry] from this repository.
     *
     * @param entry the entry to delete from this repository.
     */
    suspend fun delete(entry: IdentifiedEntry)

    /**
     * Updates the term or the definition of the entry that is identified by
     * the identifier of [entry] with the term and definition values of [entry].
     */
    suspend fun update(entry: IdentifiedEntry)

    /**
     * Sets the value of [entryToEdit] to null.
     */
    fun clearEntryToEdit()
}
