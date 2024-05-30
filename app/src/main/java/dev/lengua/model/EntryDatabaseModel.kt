package dev.lengua.model

import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry

data class EntryDatabaseModel(
    val id: Int,
    val term: String,
    val definition: String
) {
    fun asEntry() = Entry(term, definition)
    fun asIdentifiedEntry() = IdentifiedEntry(id, term, definition)
}
