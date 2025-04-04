package com.rndeveloper.myapplication.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rndeveloper.myapplication.ui.screens.home.HomeAction
import com.rndeveloper.myapplication.ui.screens.home.HomeViewModel

@Composable
fun FavCitiesContent(
    state: HomeViewModel.UiState,
    onAction: (HomeAction) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(state.favCities) { city ->
            Card(
                onClick = { onAction(HomeAction.OnSelectedCity(city)) },
                colors = CardDefaults.cardColors(
                    containerColor = if (city == state.selectedCity) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = city.name,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = Icons.Default.Close.toString(),
                        modifier = Modifier
                            .size(14.dp)
                            .clickable {
                                onAction(HomeAction.OnToggleCity(city, true))
                            }
                    )
                }
            }
        }
    }
}