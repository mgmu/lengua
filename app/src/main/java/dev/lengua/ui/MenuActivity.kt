package dev.lengua.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.lengua.LenguaApp
import dev.lengua.ui.theme.LenguaTheme

class MenuActivity: ComponentActivity() {
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LenguaTheme {
                val entries by viewModel.allEntries.observeAsState(listOf())
                MenuScreen("Add entry", ::startAddEntryActivity, entries)
            }
        }
    }

    private fun startAddEntryActivity() {
        val intent = Intent(this, AddEntryActivity::class.java)
        startActivity(intent)
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMenuScreen() {
        val fakeData = listOf(
            IdentifiedEntry(0, "aloha", "\"hello\" in hawai language"),
            IdentifiedEntry(1, "hello", "\"aloha\" in english language"),
            IdentifiedEntry(2, "hola", "\"bonjour\" in spanish language"),
            IdentifiedEntry(3, "bonjour", "\"hola\" in french language"),
        )
        LenguaTheme {
            MenuScreen("Add entry", {}, fakeData)
        }
    }

    @Composable
    fun MenuScreen(
        addEntryButtonText: String,
        addEntryButtonOnClick: () -> Unit,
        entries: List<IdentifiedEntry>
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (entries.isEmpty()) {
                    Text("No entries !")
                } else {
                    LazyColumn {
                        items(entries) {
                            ListItem(it)
                        }
                    }
                }
                Button(onClick = addEntryButtonOnClick) {
                    Text(addEntryButtonText)
                }
            }
        }
    }

    @Composable
    fun ListItem(entry: IdentifiedEntry) {
        Column {
            Text(entry.term())
            Text(entry.definition())
            Divider()
        }
    }
}