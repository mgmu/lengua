package dev.lengua.model

import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry

/**
 * An entry as stored in the application database.
 *
 * @property id the identifier of the entry provided by the application
 * database.
 * @property term the text of the term saved in the application database.
 * @property definition the text of the definition saved in the application
 * database.
 */
data class EntryDatabaseModel(
    val id: Long,
    val term: String,
    val definition: String
) {
    fun asEntry() = Entry(term, definition)
    fun asIdentifiedEntry() = IdentifiedEntry(id, term, definition)
}
