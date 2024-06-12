package dev.lengua.model

import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.transform

/**
 * The default repository of entries.
 *
 * This repository provides access to all the entries and the entry to edit
 * along methods to add, update or delete such entries.
 *
 * @param entryDao the data access object to entries stored in the application
 * database
 */
class DefaultEntriesRepository(
    private val entryDao: EntryDao
): EntriesRepository {
    override var entryToEdit: IdentifiedEntry? = null

    override val allEntries = entryDao.getAll().transform {
        val entries = mutableListOf<IdentifiedEntry>()
        for (model in it)
            entries.add(model.asIdentifiedEntry())
        emit(entries)
    }

    override suspend fun add(entry: Entry) {
        entryDao.insert(entry)
    }

    override suspend fun delete(entry: IdentifiedEntry) {
        val model = EntryDatabaseModel(
            entry.id(),
            entry.term(),
            entry.definition()
        )
        entryDao.delete(model)
    }

    override suspend fun update(entry: IdentifiedEntry) {
        val model = EntryDatabaseModel(
            entry.id(),
            entry.term(),
            entry.definition()
        )
        entryDao.update(model)
    }

    override fun clearEntryToEdit() {
        this.entryToEdit = null
    }
}
