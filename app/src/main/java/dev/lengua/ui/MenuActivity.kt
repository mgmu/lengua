package dev.lengua.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.lengua.ui.theme.LenguaTheme

class MenuActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LenguaTheme {
                MenuScreen()
            }
        }
    }

    private fun startMenuActivity() {
        val intent = Intent(this, AddEntryActivity::class.java)
        startActivity(intent)
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMenuScreen() {
        LenguaTheme {
            MenuScreen()
        }
    }

    @Composable
    fun MenuScreen() {
        Button(onClick = ::startMenuActivity) {
            Text("Add entry")
        }
    }
}