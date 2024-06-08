package dev.lengua.ui

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