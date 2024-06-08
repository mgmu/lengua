package dev.lengua.model

import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DefaultEntriesRepositoryTest {

    private val emptyDao = object : EntryDao {
        override fun getAll(): Flow<List<EntryDatabaseModel>> {
            return flow {
                emit(listOf())
            }
        }

        override suspend fun insert(entry: Entry) {
            TODO("Not yet implemented")
        }

        override suspend fun delete(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }

        override suspend fun update(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }
    }

    private val daoWith3Elements = object : EntryDao {
        val fakeData = mutableListOf(
            EntryDatabaseModel(0, "term0", "def0"),
            EntryDatabaseModel(1, "term1", "def1"),
            EntryDatabaseModel(2, "term2", "def2")
        )

        override fun getAll(): Flow<List<EntryDatabaseModel>> {
            return flow {
                emit(fakeData)
            }
        }

        override suspend fun insert(entry: Entry) {
            fakeData.add(EntryDatabaseModel(3, entry.term(), entry.definition()))
        }

        override suspend fun delete(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }

        override suspend fun update(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }
    }

    @Test
    fun `All entries is side 3 and contains same elements if database contains these 3 elements`() = runTest {
        val expected = listOf(
            IdentifiedEntry(0, "term0", "def0"),
            IdentifiedEntry(1, "term1", "def1"),
            IdentifiedEntry(2, "term2", "def2")
        )
        val sut = DefaultEntriesRepository(daoWith3Elements)
        sut.allEntries.collect {
            assertEquals(expected, it)
        }
    }

    @Test
    fun `All entries is empty if database contains 0 entries`() = runTest {
        val expected = listOf<IdentifiedEntry>()
        val sut = DefaultEntriesRepository(emptyDao)
        sut.allEntries.collect {
            assertEquals(expected, it)
        }
    }

    @Test
    fun `If database contains 3 entries, it contains 4 entries after successful insert`() = runTest {
        val sut = DefaultEntriesRepository(daoWith3Elements)
        val entry = Entry("term3", "def3")
        val expected = listOf(
            IdentifiedEntry(0, "term0", "def0"),
            IdentifiedEntry(1, "term1", "def1"),
            IdentifiedEntry(2, "term2", "def2"),
            IdentifiedEntry(3, "term3", "def3")
        )
        sut.add(entry)
        sut.allEntries.collect {
            assertEquals(expected, it)
        }
    }

    @Test
    fun `entryToEdit is null on repository creation`() {
        val sut = DefaultEntriesRepository(emptyDao)
        assertTrue(sut.entryToEdit == null)
    }
}