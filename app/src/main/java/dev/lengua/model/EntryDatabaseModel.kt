package dev.lengua.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry

@Entity(tableName = "entry")
data class EntryDatabaseModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val term: String,
    val definition: String
) {
    fun asEntry() = Entry(term, definition)
    fun asIdentifiedEntry() = IdentifiedEntry(id, term, definition)
}
