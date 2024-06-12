package dev.lengua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.lengua.LenguaApp
import dev.lengua.model.EntriesRepository
import kotlinx.coroutines.launch

class MenuViewModel(private val entriesRepo: EntriesRepository): ViewModel() {
    private var entryToDelete: IdentifiedEntry? = null

    val allEntries = entriesRepo.allEntries.asLiveData()

    fun deleteEntry() {
        entryToDelete ?: return
        viewModelScope.launch {
            entriesRepo.delete(entryToDelete!!)
        }
    }

    fun setEntryToEdit(entryToEdit: IdentifiedEntry) {
        entriesRepo.entryToEdit = entryToEdit
    }

    fun setEntryToDelete(entryToDelete: IdentifiedEntry?) {
        this.entryToDelete = entryToDelete
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as LenguaApp)
                val entriesRepo = app.container.entriesRepository
                MenuViewModel(entriesRepo)
            }
        }
    }
}
