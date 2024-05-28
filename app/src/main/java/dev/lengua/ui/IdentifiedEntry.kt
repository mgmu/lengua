package dev.lengua.ui

data class IdentifiedEntry(
    private val id: Int,
    val term: String,
    val definition: String)
{
    private val entry = Entry(term, definition)

    fun id() = id
    fun term() = entry.term()
    fun definition() = entry.definition()
}