package dev.lengua.ui

import android.content.res.Configuration
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
import dev.lengua.ui.components.EditableEntryWithButton
import dev.lengua.ui.theme.LenguaTheme

class EditEntryActivity: ComponentActivity() {
    private val viewModel: EditEntryViewModel by viewModels {
        EditEntryViewModel.Factory
    }

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
                    "Edit",
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
        title: String,
        term: String,
        onTermValueChange: (String) -> Unit,
        definition: String,
        onDefinitionValueChange: (String) -> Unit,
        onClick: () -> Unit
    ) {
        EditableEntryWithButton(
            title,
            term,
            onTermValueChange,
            definition,
            onDefinitionValueChange,
            onClick,
            "Save",
            "edit term",
            "edit definition"
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
    fun EditEntryActivityScreenPreview() {
        LenguaTheme {
            EditEntryActivityScreen(
                "Edit",
                "preview term",
                {},
                "preview definition",
                {},
                {}
            )
        }
    }
}