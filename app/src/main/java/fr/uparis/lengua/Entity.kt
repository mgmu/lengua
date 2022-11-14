package fr.uparis.lengua

import androidx.room.Entity

@Entity(primaryKeys = ["name", "link"])
data class Dictionary(
    var name: String,
    var link: String
)

@Entity(primaryKeys = ["word", "sourceLanguage", "destinationLanguage"])
data class Word(
    var word: String,
    var sourceLanguage: String,
    var destinationLanguage: String,
    var link: String
)
