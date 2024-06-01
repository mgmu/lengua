package dev.lengua.model

import androidx.lifecycle.MutableLiveData
import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.Flow

interface EntriesRepository {

    var entryToEdit: MutableLiveData<IdentifiedEntry?>

    val allEntries: Flow<List<IdentifiedEntry>>

    suspend fun add(entry: Entry)

    suspend fun delete(entry: IdentifiedEntry)

    suspend fun update(entry: IdentifiedEntry)

    fun setEntryToEdit(entryToEdit: IdentifiedEntry)

    fun clearEntryToEdit()
}