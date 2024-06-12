package dev.lengua.model

import dev.lengua.ui.Entry
import dev.lengua.ui.IdentifiedEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DefaultEntriesRepositoryTest {

    private lateinit var sut: DefaultEntriesRepository

    private val emptyDao = object : EntryDao {
        var id: Long = 0
        val fakeData = mutableListOf<EntryDatabaseModel>()
        override fun getAll(): Flow<List<EntryDatabaseModel>> {
            return flow {
                emit(listOf())
            }
        }

        override suspend fun insert(entry: Entry) {
            fakeData.add(
                EntryDatabaseModel(
                    id,
                    entry.term(),
                    entry.definition()
                )
            )
            id++
        }

        override suspend fun delete(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }

        override suspend fun update(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }
    }

    private val daoWith3Elements = object : EntryDao {
        var id: Long = 3
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
            fakeData.add(
                EntryDatabaseModel(
                    id,
                    entry.term(),
                    entry.definition()
                )
            )
            id++
        }

        override suspend fun delete(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }

        override suspend fun update(entry: EntryDatabaseModel) {
            TODO("Not yet implemented")
        }
    }

    @Nested
    @DisplayName("Given a repository with three entries")
    inner class RepoWith3Entries {

        @BeforeEach
        fun setup() {
            sut = DefaultEntriesRepository(daoWith3Elements)
        }

        @Test
        fun `Then allEntries content equals the three entries in the repository`() = runTest {
            val expected = listOf(
                IdentifiedEntry(0, "term0", "def0"),
                IdentifiedEntry(1, "term1", "def1"),
                IdentifiedEntry(2, "term2", "def2")
            )
            sut.allEntries.collect {
                assertEquals(expected, it)
            }
        }

        @Test
        fun `When add entry to repository, Then allEntries content equals three entries plus inserted entry`() = runTest {
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
    }

    @Nested
    @DisplayName("Given an empty repository")
    inner class EmptyRepo {

        @BeforeEach
        fun setup() {
            sut = DefaultEntriesRepository(emptyDao)
        }

        @Test
        fun `Then allEntries is empty`() = runTest {
            sut.allEntries.collect {
                assertTrue(it.isEmpty())
            }
        }
    }

    @Nested
    @DisplayName("Given a new empty repository")
    inner class NewEmptyRepo {

        @BeforeEach
        fun setup() {
            sut = DefaultEntriesRepository(emptyDao)
        }

        @Test
        fun `Then entryToEdit is null`() {
            val sut = DefaultEntriesRepository(emptyDao)
            assertTrue(sut.entryToEdit == null)
        }
    }
}
