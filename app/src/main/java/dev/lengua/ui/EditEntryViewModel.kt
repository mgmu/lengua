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

class EditEntryViewModel(
    private val entriesRepo: EntriesRepository
): ViewModel() {
    val entryToEdit = entriesRepo.entryToEdit

    fun updateEntry(editedTerm: String, editedDefinition: String): Boolean {
        if (entryToEdit != null) {
            try {
                val updatedEntry = IdentifiedEntry(
                    entryToEdit.id(),
                    editedTerm,
                    editedDefinition
                )
                viewModelScope.launch {
                    entriesRepo.update(updatedEntry)
                }
                entriesRepo.clearEntryToEdit()
                return true
            } catch (iae: IllegalArgumentException) {
                return false
            }
        }
        return false
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as LenguaApp)
                val entriesRepo = app.container.entriesRepository
                EditEntryViewModel(entriesRepo)
            }
        }
    }
}