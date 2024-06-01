package dev.lengua.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lengua.ui.theme.LenguaTheme

@Composable
fun SimpleButton(onClick: () -> Unit, modifier: Modifier, text: String) {
    Button(
        onClick,
        modifier
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
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
fun PreviewSimpleButton() {
    LenguaTheme {
        SimpleButton({}, Modifier.padding(0.dp), "Button")
    }
}
