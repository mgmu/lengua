package dev.lengua.ui

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun EntryElementTextField(
    text: String,
    onValueChange: (String) -> Unit,
    placeholderText: String
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText) }
    )
}