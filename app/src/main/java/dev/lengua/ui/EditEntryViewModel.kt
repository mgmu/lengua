package dev.lengua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lengua.model.EntriesRepository
import kotlinx.coroutines.launch

class EditEntryViewModel: ViewModel() {
    private val entriesRepo = EntriesRepository.instance()
    val entryToEdit = entriesRepo.entryToEdit

    fun updateEntry(editedTerm: String, editedDefinition: String) {
        if (entryToEdit.value != null) {
            val updatedEntry = entryToEdit.value!!.copy(
                term = editedTerm,
                definition = editedDefinition
            )
            viewModelScope.launch {
                entriesRepo.update(updatedEntry)
            }
            entriesRepo.clearEntryToEdit()
        }
    }
}