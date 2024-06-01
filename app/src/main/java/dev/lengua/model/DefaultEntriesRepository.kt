package dev.lengua.model

import androidx.lifecycle.MutableLiveData
import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.transform

class DefaultEntriesRepository(
    private val entryDao: EntryDao
): EntriesRepository {
    override var entryToEdit = MutableLiveData<IdentifiedEntry?>(null)

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

    override fun setEntryToEdit(entryToEdit: IdentifiedEntry) {
        this.entryToEdit.postValue(entryToEdit)
    }

    override fun clearEntryToEdit() {
        this.entryToEdit.postValue(null)
    }
}