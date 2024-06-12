package dev.lengua.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lengua.LenguaIcons
import dev.lengua.ui.IdentifiedEntry
import dev.lengua.ui.theme.LenguaTheme

@Composable
fun EntryListItem(
    entry: IdentifiedEntry,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(PaddingValues(start = 4.dp, top = 4.dp, bottom = 4.dp))
        ) {
            var isExpanded by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(4.dp)
                .weight(1.0f)
            ) {
                Text(
                    text = entry.term(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = entry.definition(),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(horizontalArrangement = Arrangement.End) {
                EntryListItemIconButton(onDeleteClick, LenguaIcons.Delete, null)
                EntryListItemIconButton(onEditClick, LenguaIcons.Edit, null)
            }
        }
    }
}

@Composable
fun EntryListItemIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
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
fun EntryListItemPreview() {
    val fakeData = IdentifiedEntry(0, "fake term", "fake definition")
    LenguaTheme {
        EntryListItem(fakeData, {}, {})
    }
}
