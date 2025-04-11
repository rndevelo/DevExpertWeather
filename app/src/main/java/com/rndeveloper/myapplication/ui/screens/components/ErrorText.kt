package com.rndeveloper.myapplication.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorText(error: Throwable, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.error) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = Icons.Default.Info.name,
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = error.localizedMessage ?: "An error occurred",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}