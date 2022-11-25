package fr.uparis.lengua

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface IDao {

    @Insert(entity = Word::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertWord(word: Word): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDictionary(dictionary: Dictionary): Long

    @Query("SELECT * FROM Word")
    fun loadAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM Dictionary")
    fun loadAllDictionaries(): LiveData<List<Dictionary>>

    @Update
    fun updateWord(word: Word): Int

    @Query("SELECT * FROM Word WHERE word LIKE :word||'%'")
    fun selectWord(word:String): LiveData<Word>
}