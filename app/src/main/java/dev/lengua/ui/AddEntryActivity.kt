package dev.lengua.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import dev.lengua.ui.components.EditableEntryWithButton
import dev.lengua.ui.theme.LenguaTheme

class AddEntryActivity: ComponentActivity() {
    private val viewModel: AddEntryViewModel by viewModels {
        AddEntryViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LenguaTheme {
                var term by rememberSaveable { mutableStateOf("") }
                var definition by rememberSaveable { mutableStateOf("") }
                AddEntryScreen(
                    "New term",
                    term,
                    { term = it },
                    definition,
                    { definition = it },
                    {
                        viewModel.addEntry(term, definition)
                        finish()
                    },
                    "Save",
                    "term",
                    "definition"
                )
            }
        }
    }

    @Composable
    fun AddEntryScreen(
        title: String,
        term: String,
        onTermValueChange: (String) -> Unit,
        definition: String,
        onDefinitionValueChange: (String) -> Unit,
        onClick: () -> Unit,
        buttonText: String,
        placeholderTerm: String,
        placeholderDef: String
    ) {
        EditableEntryWithButton(
            title,
            term,
            onTermValueChange,
            definition,
            onDefinitionValueChange,
            onClick,
            buttonText,
            placeholderTerm,
            placeholderDef
        )
    }

    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark"
    )
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        name = "DefaultPreviewLight"
    )
    @Composable
    fun PreviewAddEntryScreen() {
        LenguaTheme {
            AddEntryScreen(
                "New term",
                "term",
                {},
                "definition",
                {},
                {},
                "Save",
                "term",
                "definition"
            )
        }
    }
}