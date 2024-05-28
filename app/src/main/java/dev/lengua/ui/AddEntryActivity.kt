package dev.lengua.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.lengua.ui.theme.LenguaTheme

class AddEntryActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = AddEntryViewModel()

        setContent {
            LenguaTheme {
                var term by rememberSaveable { mutableStateOf("") }
                var definition by rememberSaveable { mutableStateOf("") }
                AddEntryScreen(
                    term,
                    { term = it },
                    definition,
                    { definition = it },
                    { viewModel.addEntry(term, definition) },
                    "Add"
                )
            }
        }
    }

    @Composable
    fun AddEntryScreen(
        term: String,
        onTermValueChange: (String) -> Unit,
        definition: String,
        onDefinitionValueChange: (String) -> Unit,
        onClick: () -> Unit,
        buttonText: String
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EntryElementTextField(term, onTermValueChange)
                EntryElementTextField(definition, onDefinitionValueChange)
                Button(onClick) {
                    Text(buttonText)
                }
            }
        }
    }

    @Composable
    fun EntryElementTextField(text: String, onValueChange: (String) -> Unit ) {
        TextField(text, onValueChange)
    }

    @Preview
    @Composable
    fun PreviewAddEntryScreen() {
        LenguaTheme {
            AddEntryScreen("term", {}, "definition", {}, {}, "Add")
        }
    }
}