package dev.lengua.ui.components

import android.content.res.Configuration
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.lengua.LenguaIcons
import dev.lengua.ui.theme.LenguaTheme

@Composable
fun ConfirmDeleteEntryDialog(
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    AlertDialog(
        icon = { Icon(LenguaIcons.Delete, "Delete icon") },
        title = {
            Text(
                text = "Delete confirmation",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "The entry will be forever lost. This action can not " +
                        "be undone.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
                Text("Dismiss")
            }
        }
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
fun ConfirmDeleteEntryDialogPreview() {
    LenguaTheme {
        ConfirmDeleteEntryDialog({}, {}, {})
    }
}
