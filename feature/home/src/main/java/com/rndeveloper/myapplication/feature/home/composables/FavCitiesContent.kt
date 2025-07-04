package com.rndeveloper.myapplication.feature.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.feature.home.HomeAction

@Composable
fun FavCitiesContent(
    favCities: List<City>,
    selectedCity: City?,
    onAction: (HomeAction) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = favCities,
            key = { city -> city.name }
        ) { city ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(300)) + scaleIn(tween(300)),
                exit = fadeOut(tween(300)) + scaleOut(tween(300)),
                modifier = Modifier.animateItem()
            ) {
                Card(
                    onClick = { onAction(HomeAction.OnSelectedCity(city)) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (city == selectedCity)
                            MaterialTheme.colorScheme.primaryContainer
                        else Color.Unspecified
                    )
                ) {
                    Text(
                        text = city.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
