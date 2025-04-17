package com.rndeveloper.myapplication.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.home.HomeAction
import com.rndeveloper.myapplication.home.R

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