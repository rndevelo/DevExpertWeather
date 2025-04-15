package com.rndeveloper.myapplication.ui.screens.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rndeveloper.myapplication.R
import com.rndeveloper.myapplication.domain.City
import com.rndeveloper.myapplication.ui.screens.home.HomeAction

@Composable
fun ShowDialogIfPermissionIsDenied(
    selectedCity: City?,
    favCities: List<City>,
    searchedCities: List<City>,
    isLocationPermissionDenied: Boolean,
    onIsLocationPermissionDenied: () -> Unit,
    onAction: (HomeAction) -> Unit
) {
    AnimatedVisibility(isLocationPermissionDenied) {
        AlertDialog(
            onDismissRequest = onIsLocationPermissionDenied,
            title = { Text(stringResource(R.string.home_text_enter_a_city)) },
            text = {
                if (selectedCity == null) {
                    SearchContent(
                        favCities = favCities,
                        searchedCities = searchedCities,
                        onAction = onAction
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedCity != null) {
                            onIsLocationPermissionDenied()
                        }
                    },
                    enabled = selectedCity != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.home_text_accept))
                }
            }
        )
    }
}