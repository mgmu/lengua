package dev.lengua.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import dev.lengua.ui.theme.LenguaTheme

class EditEntryActivity: ComponentActivity() {
    private val viewModel: EditEntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LenguaTheme {
                val entryToEdit by viewModel.entryToEdit.observeAsState()
                val initialTerm =
                    if (entryToEdit == null)
                        "Nothing to edit"
                    else
                        entryToEdit!!.term()
                val initialDefinition =
                    if (entryToEdit == null)
                        "Nothing to edit"
                    else
                        entryToEdit!!.definition()
                var term by rememberSaveable {
                    mutableStateOf(initialTerm)
                }
                var definition by rememberSaveable {
                    mutableStateOf(initialDefinition)
                }
                EditEntryActivityScreen(
                    term,
                    { term = it },
                    definition,
                    { definition = it},
                    {
                        viewModel.updateEntry(term, definition)
                        finish()
                    }
                )
            }
        }
    }

    @Composable
    fun EditEntryActivityScreen(
        term: String,
        onTermValueChange: (String) -> Unit,
        definition: String,
        onDefinitionValueChange: (String) -> Unit,
        onClick: () -> Unit
    ) {
        EntryDescriptionWithButton(
            term,
            onTermValueChange,
            definition,
            onDefinitionValueChange,
            onClick,
            "Edit",
            "edit term",
            "edit definition"
        )
    }

    @Preview
    @Composable
    fun EditEntryActivityScreenPreview() {
        EditEntryActivityScreen(
            "preview term",
            {},
            "preview definition",
            {},
            {}
        )
    }
}