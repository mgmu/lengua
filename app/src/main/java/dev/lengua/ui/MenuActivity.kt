package dev.lengua.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lengua.ui.components.EntryList
import dev.lengua.ui.components.SimpleButton
import dev.lengua.ui.theme.LenguaTheme

class MenuActivity: ComponentActivity() {
    private val viewModel: MenuViewModel by viewModels {
        MenuViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LenguaTheme {
                val entries by viewModel.allEntries.observeAsState(listOf())
                MenuScreen(
                    "Add new term",
                    ::startAddEntryActivity,
                    entries,
                    ::setEntryToDelete,
                    ::editEntry,
                    { viewModel.setEntryToDelete(null) },
                    { viewModel.deleteEntry() }
                )
            }
        }
    }

    private fun startAddEntryActivity() {
        val intent = Intent(this, AddEntryActivity::class.java)
        startActivity(intent)
    }

    private fun editEntry(entryToEdit: IdentifiedEntry) {
        viewModel.setEntryToEdit(entryToEdit)
        val intent = Intent(this, EditEntryActivity::class.java)
        startActivity(intent)
    }

    private fun setEntryToDelete(entryToDelete: IdentifiedEntry) {
        viewModel.setEntryToDelete(entryToDelete)
    }

    @Composable
    fun MenuScreen(
        addEntryButtonText: String,
        addEntryButtonOnClick: () -> Unit,
        entries: List<IdentifiedEntry>,
        onDeleteIconButtonClick: (IdentifiedEntry) -> Unit,
        onEditIconButtonClick: (IdentifiedEntry) -> Unit,
        onDismissDialog: () -> Unit,
        onConfirmDialog: () -> Unit
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Terms",
                    modifier = Modifier.padding(PaddingValues(top = 8.dp)),
                    style = MaterialTheme.typography.headlineSmall
                )
                Divider(modifier = Modifier.padding(8.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (entries.isEmpty()) {
                        Text("No terms !")
                    } else {
                        EntryList(
                            entries,
                            onDeleteIconButtonClick,
                            onEditIconButtonClick,
                            onDismissDialog,
                            onConfirmDialog
                        )
                    }
                }
                Divider(modifier = Modifier.padding(8.dp))
                val addEntryButtonModifier = Modifier.padding(
                    PaddingValues(bottom = 8.dp)
                )
                SimpleButton(
                    addEntryButtonOnClick,
                    addEntryButtonModifier,
                    addEntryButtonText
                )
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
    fun PreviewMenuScreenWithEntries() {
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
            )
        )
        LenguaTheme {
            MenuScreen("Add new term", {}, fakeData, {}, {}, {}, {})
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
    fun PreviewMenuScreenWithoutEntries() {
        LenguaTheme {
            MenuScreen("Add new term", {}, listOf(), {}, {}, {}, {})
        }
    }
}