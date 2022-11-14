package fr.uparis.lengua

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertWord(word: Word): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertDictionary(dictionary: Dictionary): Long

    @Query("SELECT * FROM Word")
    fun loadAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM Dictionary")
    fun loadAllDictionaries(): LiveData<List<Dictionary>>
}