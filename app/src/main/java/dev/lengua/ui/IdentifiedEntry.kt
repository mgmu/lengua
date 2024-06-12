package dev.lengua.ui

/**
 * An entry with an identifier.
 *
 * The identifier is a long integer that uniquely identifies an entry.
 *
 * @property id the unique identifier of this entry.
 * @property term the term of this entry.
 * @property definition the definition of this entry.
 */
data class IdentifiedEntry(
    private val id: Long,
    val term: String,
    val definition: String
) {
    // The entry to identify
    private val entry = Entry(term, definition)

    fun id() = id
    fun term() = entry.term()
    fun definition() = entry.definition()
}
