package dev.lengua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.lengua.LenguaApp
import dev.lengua.model.EntriesRepository
import kotlinx.coroutines.launch

class AddEntryViewModel(
    private val entriesRepo: EntriesRepository
): ViewModel() {

    /**
     * Adds a new entry to the entry repository.
     *
     * If [term] or [definition] are invalid, the entry is not added.
     *
     * @param term the term of the new entry
     * @param definition the definition of the new entry
     *
     * @return true if the entry was added to the repository
     */
    fun addEntry(term: String, definition: String): Boolean {
        try {
            val entry = Entry(term, definition)
            viewModelScope.launch {
                entriesRepo.add(entry)
            }
            return true
        } catch (iae: IllegalArgumentException) {
            return false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as LenguaApp)
                val entriesRepo = app.container.entriesRepository
                AddEntryViewModel(entriesRepo)
            }
        }
    }
}
