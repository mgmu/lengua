package fr.uparis.lengua

import android.app.Application

class TranslationApplication: Application() {
    val database by lazy { TranslationDatabase.getDatabase(this) }
}