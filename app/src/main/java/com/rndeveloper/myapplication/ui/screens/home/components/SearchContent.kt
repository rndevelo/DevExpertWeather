package com.rndeveloper.myapplication.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.rndeveloper.myapplication.R
import com.rndeveloper.myapplication.data.CityInfo
import com.rndeveloper.myapplication.ui.screens.home.HomeAction

@Composable
fun SearchContent(
    keyboardController: SoftwareKeyboardController?,
    citiesInfo: List<CityInfo>,
    onAction: (HomeAction) -> Unit,
) {
    var cityName by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(emptyList<CityInfo>()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = cityName,
            onValueChange = { newText ->
                cityName = newText
                onAction(HomeAction.OnSearchCities(newText))
                suggestions = citiesInfo
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

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(suggestions) { city ->
                Text(
                    text = "📍 ${city.name}, ${city.country}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onAction(
                                HomeAction.OnSelectedCityInfo(
                                    CityInfo().copy(
                                        name = city.name,
                                        country = city.country,
                                        latitude = city.latitude,
                                        longitude = city.longitude
                                    )
                                )
                            )
                            suggestions = emptyList()
                            keyboardController?.hide()
                            cityName = ""
                        })
                        .padding(12.dp)
                )
            }
        }
    }
}
