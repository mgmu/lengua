package dev.lengua

import android.app.Application
import dev.lengua.model.AppDatabase

class LenguaApp: Application() {

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.instance(this)
    }

    companion object {
        private lateinit var database: AppDatabase

        fun entryDao() = database.entryDao()
    }
}