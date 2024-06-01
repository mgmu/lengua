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
     * @param term the term of the new entry
     * @param definition the definition of the new entry
     */
    fun addEntry(term: String, definition: String) {
        viewModelScope.launch {
            entriesRepo.add(Entry(term, definition))
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