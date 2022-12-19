package fr.uparis.lengua

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread

class TranslationViewModel(app: Application): AndroidViewModel(app) {

    /**
     * DB Request interface
     */
    private val dao = (app as TranslationApplication).database.iDao()

    /**
     * All dictionaries of database
     */
    var allDictionaries: LiveData<List<Dictionary>> = loadAllDictionaries()

    /**
     * All words of database
     */
    var allWords: LiveData<List<Word>> = loadAllWords()

    /**
     * Selected dictionary
     */
    var selectedDictionary = MutableLiveData<Dictionary>()

    /**
     * Selected word
     */
    var selectedWord = MutableLiveData<Word>()

    /**
     * Keeps track of the word insertion result
     */
    val insertWordResult = MutableLiveData<Long>()

    /**
     * Keeps track of the dictionary insertion result
     */
    val insertDictionaryResult = MutableLiveData<Long>()

    /**
     * Loads all dictionaries in database
     */
    fun loadAllDictionaries() = dao.loadAllDictionaries()

    /**
     * Loads all words in database
     */
    fun loadAllWords() = dao.loadAllWords()

    /**
     * Returns true if a dictionary is selected.
     */
    fun isDictionarySelected(): Boolean = selectedDictionary.value != null

    /**
     * Returns true if a word is selected.
     */
    fun isWordSelected(): Boolean = selectedWord.value != null

    /**
     * Inserts a word in the database
     * @return the id of the inserted word or -1 if insertion failed
     */
    fun insertWord(word: Word) {
        thread {
            insertWordResult.postValue(dao.insertWord(word))
        }
    }

    /**
     * Inserts a dictionary in the database
     *  @return the id of the inserted dictionary or -1 if insertion failed
     */
    fun insertDictionary(dict: Dictionary) {
        thread {
            insertDictionaryResult.postValue(dao.insertDictionary(dict))
        }
    }

    /* SaveSearchFragment EditText values saved on configuration change. */
    var dictionaryName: String? = null
    var wordToSave: String? = null
    var srcLanguage: String? = null
    var destLanguage: String? = null
}
