package dev.lengua.model

import dev.lengua.LenguaApp
import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class EntriesRepository private constructor() {
    private val entryDao = LenguaApp.entryDao()

    val allEntries: Flow<List<IdentifiedEntry>> = entryDao.getAll().transform {
        val entries = mutableListOf<IdentifiedEntry>()
        for (model in it)
            entries.add(model.asIdentifiedEntry())
        emit(entries)
    }

    suspend fun add(entry: Entry) {
        entryDao.insert(entry)
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