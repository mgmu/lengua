package dev.lengua.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lengua.R
import dev.lengua.ui.theme.LenguaTheme

@Composable
fun EditableEntryWithButtonPortrait(
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
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EntryDescriptionWithFlag(
                false,
                title,
                term,
                onTermValueChange,
                definition,
                onDefinitionValueChange,
                placeholderTerm,
                placeholderDef
            )
            val buttonModifier = Modifier.padding(32.dp)
            SimpleButton(onClick, buttonModifier, buttonText)
        }
    }
}

@Composable
fun EditableEntryWithButtonLandscape(
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
    Surface(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EntryDescriptionWithFlag(
                    false,
                    title,
                    term,
                    onTermValueChange,
                    definition,
                    onDefinitionValueChange,
                    placeholderTerm,
                    placeholderDef
                )
            }
            val buttonModifier = Modifier.padding(32.dp)
            SimpleButton(onClick, buttonModifier, buttonText)
        }
    }
}

@Composable
private fun EntryDescriptionWithFlag(
    readOnly: Boolean,
    title: String,
    term: String,
    onTermValueChange: (String) -> Unit,
    definition: String,
    onDefinitionValueChange: (String) -> Unit,
    placeholderTerm: String,
    placeholderDef: String
) {
    Text(text = title, style = MaterialTheme.typography.headlineSmall)
    Divider(modifier = Modifier
        .padding(8.dp)
        .width(316.dp))
    OutlinedTextField(
        value = term,
        onValueChange = onTermValueChange,
        placeholder = { Text(placeholderTerm) },
        label = @Composable { Text(stringResource(R.string.term)) },
        modifier = Modifier
            .height(65.dp)
            .width(300.dp),
        readOnly = readOnly
    )
    OutlinedTextField(
        value = definition,
        onValueChange = onDefinitionValueChange,
        placeholder = { Text(placeholderDef) },
        label = @Composable { Text(stringResource(R.string.definition)) },
        modifier = Modifier
            .height(200.dp)
            .width(300.dp),
        readOnly = readOnly
    )
}

@Composable
fun EntryDescription(
    title: String,
    term: String,
    definition: String,
    placeholderTerm: String,
    placeholderDef: String
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EntryDescriptionWithFlag(
                true,
                title,
                term,
                {},
                definition,
                {},
                placeholderTerm,
                placeholderDef
            )
        }
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
fun PreviewEntryDescription() {
    LenguaTheme {
        EntryDescription(
            "Entry",
            "the term",
            "the definition",
            "placeholder term",
            "placeholder definition"
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDarkPortrait"
)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLightPortrait"
)
@Composable
fun PreviewEditableEntryWithButtonPortrait() {
    LenguaTheme {
        EditableEntryWithButtonPortrait(
            "Entry",
            "the term",
            {},
            "the definition",
            {},
            {},
            "Button",
            "placeholder term",
            "placeholder definition"
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDarkLandscape",
    device = "spec:orientation=landscape,width=411dp,height=891dp"
)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLightLandscape",
    device = "spec:orientation=landscape,width=411dp,height=891dp"
)
@Composable
fun PreviewEditableEntryWithButtonLandscape() {
    LenguaTheme {
        EditableEntryWithButtonLandscape(
            "Entry",
            "the term",
            {},
            "the definition",
            {},
            {},
            "Button",
            "placeholder term",
            "placeholder definition"
        )
    }
}
