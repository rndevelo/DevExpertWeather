package com.rndeveloper.myapplication.feature.home.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun FavouriteIconButtonContent(isFav: Boolean, onSaveCity: () -> Unit) {
    IconButton(onClick = onSaveCity) {
        Icon(
            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = Icons.Default.Favorite.toString(),
        )
    }
}