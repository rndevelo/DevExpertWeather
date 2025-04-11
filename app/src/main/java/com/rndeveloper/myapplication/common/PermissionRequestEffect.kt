package com.rndeveloper.myapplication.common

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

@Composable
fun PermissionRequestEffect(permission: String, onResult: (Boolean) -> Unit) {
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            onResult(it)
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }
}

//@Composable
//fun PermissionRequestEffect(
//    permission: String,
//    onResult: (isGranted: Boolean, isPermanentlyDenied: Boolean) -> Unit
//) {
//    val context = LocalContext.current
//    val activity = context as? Activity
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        val permanentlyDenied = !isGranted && activity != null &&
//                !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
//
//        onResult(isGranted, permanentlyDenied)
//    }
//
//    LaunchedEffect(Unit) {
//        permissionLauncher.launch(permission)
//    }
//}