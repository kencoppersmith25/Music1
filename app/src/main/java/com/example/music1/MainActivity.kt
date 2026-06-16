package com.example.music1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.example.music1.RadioController
import com.example.music1.BluetoothManager
import com.example.music1.ui.theme.BluetoothScreen
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.music1.ui.theme.UserScreen
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer


class MainActivity : ComponentActivity() {
    private lateinit var controller: RadioController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player = ExoPlayer.Builder(this).build()
        controller = RadioController(this, player, RadioCatalog)
        setContent {
            UserScreen(controller)
        }
    }
}