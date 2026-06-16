package com.example.music1.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.material3.*
import com.example.music1.BluetoothManager
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.Manifest
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.compose.rememberLauncherForActivityResult

@Composable
fun BluetoothScreen() {
    val context = LocalContext.current
    val bluetoothManager = remember { BluetoothManager(context) }
//    val permissionGranted =
//        ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.BLUETOOTH_CONNECT
//        ) == PackageManager.PERMISSION_GRANTED
}
