package dev.lengua

import dev.lengua.model.AppDatabase
import dev.lengua.model.EntriesRepository

interface LenguaAppContainer {

    val database: AppDatabase
    val entriesRepository: EntriesRepository
}