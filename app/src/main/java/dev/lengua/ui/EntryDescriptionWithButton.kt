package dev.lengua.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EntryDescriptionWithButton(
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