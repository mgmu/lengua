package dev.lengua.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.lengua.model.EntriesRepository

class MenuViewModel: ViewModel() {
    private val entriesRepo = EntriesRepository.instance()

    val allEntries = entriesRepo.allEntries.asLiveData()
}