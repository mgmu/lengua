package fr.uparis.lengua

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TranslationViewModel(app: Application): AndroidViewModel(app) {

    /* DB Request interface */
    private val dao = (app as TranslationApplication).database.iDao()

    /* All dictionaries of database */
    var allDictionaries: LiveData<List<Dictionary>> = loadAllDictionaries()

    /* Selected dictionary in research page */
    var selectedDictionary = MutableLiveData<Dictionary>()

    /* Loads all dictionaries in database */
    fun loadAllDictionaries() = dao.loadAllDictionaries()

    /* Loads all words in database */
    fun loadAllWords() = dao.loadAllWords()

    /**
     * Returns true if a dictionary is selected.
     */
    fun isDictionarySelected(): Boolean = selectedDictionary.value != null
}