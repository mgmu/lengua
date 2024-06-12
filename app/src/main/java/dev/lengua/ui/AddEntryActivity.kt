package dev.lengua.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.lengua.R
import dev.lengua.ui.components.EditableEntryWithButtonLandscape
import dev.lengua.ui.components.EditableEntryWithButtonPortrait
import dev.lengua.ui.theme.LenguaTheme
import kotlinx.coroutines.launch

class AddEntryActivity: ComponentActivity() {
    private val viewModel: AddEntryViewModel by viewModels {
        AddEntryViewModel.Factory
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LenguaTheme {
                var term by rememberSaveable { mutableStateOf("") }
                var definition by rememberSaveable { mutableStateOf("") }
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) {
                            Snackbar(
                                snackbarData = it,
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                ) { _ ->
                    AddEntryScreen(
                        stringResource(R.string.new_term),
                        term,
                        { term = it },
                        definition,
                        { definition = it },
                        {
                            if (viewModel.addEntry(term, definition))
                                finish()
                            else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        getString(R.string.could_not_save_term)
                                    )
                                }
                            }
                        },
                        stringResource(R.string.save),
                        stringResource(R.string.type_term_here),
                        stringResource(R.string.type_definition_here)
                    )
                }
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
        val configuration = LocalConfiguration.current
        val isLandscape =
            configuration.orientation  == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            EditableEntryWithButtonLandscape(
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
        } else {
            EditableEntryWithButtonPortrait(
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
    }

    @Preview(
        showSystemUi = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark"
    )
    @Preview(
        showSystemUi = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        name = "DefaultPreviewLight"
    )
    @Composable
    fun PreviewAddEntryScreenPortrait() {
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

    @Preview(
        showSystemUi = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark",
        device = "spec:orientation=landscape,width=411dp,height=891dp"
    )
    @Preview(
        showSystemUi = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        name = "DefaultPreviewLight",
        device = "spec:orientation=landscape,width=411dp,height=891dp"
    )
    @Composable
    fun PreviewAddEntryScreenLandscape() {
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
