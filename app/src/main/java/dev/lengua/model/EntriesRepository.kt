package dev.lengua.model

import androidx.lifecycle.MutableLiveData
import dev.lengua.LenguaApp
import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class EntriesRepository private constructor() {
    private val entryDao = LenguaApp.entryDao()
    var entryToEdit = MutableLiveData<IdentifiedEntry?>(null)

    val allEntries: Flow<List<IdentifiedEntry>> = entryDao.getAll().transform {
        val entries = mutableListOf<IdentifiedEntry>()
        for (model in it)
            entries.add(model.asIdentifiedEntry())
        emit(entries)
    }

    suspend fun add(entry: Entry) {
        entryDao.insert(entry)
    }

    suspend fun delete(entry: IdentifiedEntry) {
        val model = EntryDatabaseModel(
            entry.id(),
            entry.term(),
            entry.definition()
        )
        entryDao.delete(model)
    }

    suspend fun update(entry: IdentifiedEntry) {
        val model = EntryDatabaseModel(
            entry.id(),
            entry.term(),
            entry.definition()
        )
        entryDao.update(model)
    }

    fun setEntryToEdit(entryToEdit: IdentifiedEntry) {
        this.entryToEdit.postValue(entryToEdit)
    }

    fun clearEntryToEdit() {
        this.entryToEdit.postValue(null)
    }

    companion object {
        private var instance: EntriesRepository? = null

        fun instance(): EntriesRepository {
            if (instance == null)
                instance = EntriesRepository()
            return instance!!
        }
    }
}