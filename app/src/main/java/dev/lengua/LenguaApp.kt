package dev.lengua

import android.app.Application
import androidx.compose.material.icons.Icons

val LenguaIcons = Icons.Rounded
const val TAG = "logcat_tag_lengua_app"

/**
 * The application.
 */
class LenguaApp: Application() {

    /**
     * The dependency container of this application.
     */
    lateinit var container: LenguaAppContainer

    override fun onCreate() {
        super.onCreate()

        container = DefaultContainer(this)
    }
}
