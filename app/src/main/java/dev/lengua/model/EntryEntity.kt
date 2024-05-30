package dev.lengua.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry")
data class EntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val term: String,
    val definition: String
)