package dev.lengua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lengua.model.EntriesRepository
import kotlinx.coroutines.launch

class AddEntryViewModel: ViewModel() {

    /**
     * Adds a new entry to the entry repository.
     *
     * @param term the term of the new entry
     * @param definition the definition of the new entry
     */
    fun addEntry(term: String, definition: String) {
        val entriesRepo = EntriesRepository.instance()
        viewModelScope.launch {
            entriesRepo.add(Entry(term, definition))
        }
    }
}