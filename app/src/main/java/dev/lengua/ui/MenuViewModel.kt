package dev.lengua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.lengua.model.EntriesRepository
import kotlinx.coroutines.launch

class MenuViewModel: ViewModel() {
    private val entriesRepo = EntriesRepository.instance()

    val allEntries = entriesRepo.allEntries.asLiveData()

    fun delete(entry: IdentifiedEntry) {
        viewModelScope.launch {
            entriesRepo.delete(entry)
        }
    }

    fun setEntryToEdit(entryToEdit: IdentifiedEntry) {
        entriesRepo.setEntryToEdit(entryToEdit)
    }
}