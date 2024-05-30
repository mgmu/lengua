package dev.lengua.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
    private val viewModel: AddEntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LenguaTheme {
                var term by rememberSaveable { mutableStateOf("") }
                var definition by rememberSaveable { mutableStateOf("") }
                AddEntryScreen(
                    term,
                    { term = it },
                    definition,
                    { definition = it },
                    {
                        viewModel.addEntry(term, definition)
                        finish()
                    },
                    "Add",
                    "term",
                    "definition"
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
        buttonText: String,
        placeholderTerm: String,
        placeholderDef: String
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EntryElementTextField(term, onTermValueChange, placeholderTerm)
                EntryElementTextField(definition, onDefinitionValueChange, placeholderDef)
                Button(onClick) {
                    Text(buttonText)
                }
            }
        }
    }

    @Composable
    fun EntryElementTextField(
        text: String,
        onValueChange: (String) -> Unit,
        placeholderText: String
    ) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholderText) }
        )
    }

    @Preview
    @Composable
    fun PreviewAddEntryScreen() {
        LenguaTheme {
            AddEntryScreen("term", {}, "definition", {}, {}, "Add", "term", "definition")
        }
    }
}