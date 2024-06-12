package dev.lengua.ui

/**
 * A term-definition entry.
 *
 * An entry is a couple formed by two strings, a term and a definition. Usually,
 * the definition is a text that defines what the term is.
 * The term must be non empty and is limited to 100 characters, the definition
 * must also be non empty but is limited to 1000 characters.
 *
 * @property term the term of this entry.
 * @property definition the definition of this entry.
 */
data class Entry(private var term: String, private var definition: String) {
    init {
        term = term.trim()
        require(term.isNotEmpty()) {
            "Term can not be empty."
        }
        definition = definition.trim()
        require(definition.isNotEmpty()) {
            "Definition can not be empty."
        }
        require(term.length <= 100) {
            "Term length must not exceed 100 characters."
        }
        require(definition.length <= 1000) {
            "Definition length must not exceed 1000 characters."
        }
    }

    fun term() = term
    fun definition() = definition
}
