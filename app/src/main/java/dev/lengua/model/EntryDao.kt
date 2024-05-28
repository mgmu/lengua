package dev.lengua.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.lengua.ui.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry")
    fun getAll(): Flow<List<EntryDatabaseModel>>

    @Insert(entity = EntryDatabaseModel::class)
    suspend fun insert(entry: Entry)
}