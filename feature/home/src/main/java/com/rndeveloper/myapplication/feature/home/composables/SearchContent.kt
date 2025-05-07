package com.rndeveloper.myapplication.feature.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.feature.home.HomeAction
import com.rndeveloper.myapplication.feature.home.R

@Composable
fun SearchContent(
    favCities: List<City>,
    searchedCities: List<City>,
    onAction: (HomeAction) -> Unit,
    keyboardController: SoftwareKeyboardController? = null,
) {
    var cityName by remember { mutableStateOf("") }

    LaunchedEffect(cityName) {
        onAction(HomeAction.OnSearchCities(cityName))
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = cityName,
            onValueChange = { newCityName ->
                cityName = newCityName
            },
            label = { Text(stringResource(R.string.home_text_search_city)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                capitalization = KeyboardCapitalization.Words
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
        )

        LazyColumn {
            items(searchedCities) { city ->
                CityItem(
                    city = city,
                    favCities = favCities,
                    onSaveCity = {
                        onAction(
                            HomeAction.OnToggleCity(
                                city,
                                favCities.contains(city)
                            )
                        )
                    },
                    onSelectedCity = {
                        cityName = ""
                        keyboardController?.hide()
                        onAction(HomeAction.OnSelectedCity(city))
                    }
                )
            }
        }
    }
}

@Composable
fun CityItem(
    city: City,
    favCities: List<City>,
    onSaveCity: () -> Unit,
    onSelectedCity: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelectedCity)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${city.name}, ${city.country}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        FavouriteIconButtonContent(isFav = favCities.contains(city), onSaveCity = onSaveCity)
    }
}
