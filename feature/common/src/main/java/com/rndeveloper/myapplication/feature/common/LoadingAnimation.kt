package com.rndeveloper.myapplication.feature.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val LOADING_ANIMATION_TAG = "LoadingAnimation"

@Composable
fun LoadingAnimation(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.testTag(LOADING_ANIMATION_TAG),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = text)
        }
    }
}