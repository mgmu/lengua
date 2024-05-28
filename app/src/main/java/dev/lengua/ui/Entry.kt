package dev.lengua.ui

data class Entry(private val term: String, private val definition: String) {
    fun term() = term
    fun definition() = definition
}