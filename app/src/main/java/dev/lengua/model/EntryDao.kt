package dev.lengua.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.lengua.ui.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry")
    fun getAll(): Flow<List<EntryDatabaseModel>>

    @Insert(entity = EntryEntity::class)
    suspend fun insert(entry: Entry)

    @Delete(entity = EntryEntity::class)
    suspend fun delete(entry: EntryDatabaseModel)

    @Update(entity = EntryEntity::class)
    suspend fun update(entry: EntryDatabaseModel)
}
