package dev.lengua.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lengua.ui.IdentifiedEntry
import dev.lengua.ui.theme.LenguaTheme

@Composable
fun EntryList(
    entries: List<IdentifiedEntry>,
    onDeleteIconButtonClick: (IdentifiedEntry) -> Unit,
    onEditIconButtonClick: (IdentifiedEntry) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val onConfirmButtonClick: () -> Unit = {
        showDialog = false
        onConfirm()
    }
    val onDismissClick: () -> Unit = {
        showDialog = false
        onDismiss()
    }
    if (showDialog) {
        ConfirmDeleteEntryDialog(
            onDismissClick,
            onConfirmButtonClick,
            onDismissClick
        )
    }
    LazyColumn(
        modifier = Modifier.height(720.dp).fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(entries) { index, item ->
            EntryListItem(
                item,
                {
                    showDialog = true
                    onDeleteIconButtonClick(item)
                },
                { onEditIconButtonClick(item) }
            )
            if (index != entries.size - 1) {
                Spacer(modifier = Modifier.padding(2.dp))
                Divider()
                Spacer(modifier = Modifier.padding(2.dp))
            }
        }
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
fun EntryListPreview() {
    val fakeData = listOf(
        IdentifiedEntry(0, "aloha", "\"hello\" in hawai language"),
        IdentifiedEntry(1, "hello", "\"aloha\" in english language"),
        IdentifiedEntry(2, "hola", "\"bonjour\" in spanish language"),
        IdentifiedEntry(3, "bonjour", "\"hola\" in french language"),
        IdentifiedEntry(
            4,
            "A term with a very long definition",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed" +
                    " do eiusmod tempor incididunt ut labore et dolore" +
                    " magna aliqua. Enim nunc faucibus a pellentesque " +
                    "sit. Porttitor eget dolor morbi non arcu risus quis." +
                    " Semper quis lectus nulla at. Viverra nam libero" +
                    " justo laoreet. Massa eget egestas purus viverra" +
                    " accumsan. Ullamcorper sit amet risus nullam eget." +
                    " Mi ipsum faucibus vitae aliquet nec ullamcorper" +
                    " sit. Sit amet venenatis urna cursus eget nunc" +
                    " scelerisque viverra mauris. Non quam lacus" +
                    " suspendisse faucibus interdum. Lectus magna" +
                    " fringilla urna porttitor rhoncus. Nunc non blandit" +
                    " massa enim nec dui nunc mattis."
        ),
        IdentifiedEntry(5, "bla", "bal"),
        IdentifiedEntry(5, "bla", "bala"),
        IdentifiedEntry(5, "bla", "heoh"),
        IdentifiedEntry(5, "bla", "hcaun"),
        IdentifiedEntry(5, "bla", "hruanp"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
        IdentifiedEntry(5, "bla", "hauionap"),
    )
    LenguaTheme {
        EntryList(fakeData, {}, {}, {}, {})
    }
}
