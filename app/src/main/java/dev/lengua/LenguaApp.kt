package dev.lengua

import android.app.Application
import androidx.compose.material.icons.Icons

val LenguaIcons = Icons.Rounded
const val TAG = "logcat_tag_lengua_app"

class LenguaApp: Application() {

    lateinit var container: LenguaAppContainer

    override fun onCreate() {
        super.onCreate()

        container = DefaultContainer(this)
    }
}