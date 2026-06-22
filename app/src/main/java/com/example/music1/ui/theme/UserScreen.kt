package com.example.music1.ui.theme

import androidx.compose.runtime.*
import androidx.media3.exoplayer.ExoPlayer
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.music1.BluetoothManager
import com.example.music1.RadioController
import com.example.music1.RadioCatalog
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import android.util.Log
import android.app.Activity
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts

//import androidx.activity.compose.rememberLauncherForActivityResult

@Composable
fun UserScreen(controller: RadioController) {
    val context = LocalContext.current
    // 🎵 Player (Compose-safe)
    val player = remember {
        ExoPlayer.Builder(context).build()
    }
    val mode by controller.currentModeName.collectAsState()
    val station by controller.currentStationName.collectAsState()
    val status by controller.playbackStatus.collectAsState()
    // 🎛 Controller (your core logic stays unchanged)
    val btManager: BluetoothManager = remember {
        BluetoothManager(context)
    }
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // -------------------
                // DISPLAY STATE
                // -------------------
                if (controller.isRecovering.value)
                    Text("Recovery Attempt: ${controller.recoveryAttempts}")
                Text(
                    if (btManager.isBluetoothAudioOn())
                        "Audio: Bluetooth"
                    else
                        "Audio: Speaker"
                )
                Text("Status: $status")
                Text("Mode: $mode")
                Text("Station: $station")
                Row {
                    Button(onClick = { controller.previousMode() }) {
                        Text("Prev Mode")
                    }
                    Button(onClick = { controller.nextMode() }) {
                        Text("Next Mode")
                    }
                }
                // -------------------
                // STATION CONTROLS
                // -------------------
                Row {
                    Button(onClick = { controller.prevStation() }) {
                        Text("Prev Station")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { controller.nextStation() }) {
                        Text("Next Station")
                    }
                }
                Row {
                    Button(onClick = { controller.nextRandomStation() }) {
                        Text("Random Station")
                    }
                }
                Row {
                    Button(onClick = { controller.play() }) {
                        Text("Play")
                    }
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Button(onClick = { controller.playCurrentStation() }) {
//                        Text("Play Stream")
//                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { controller.stop() }) {
                        Text("Stop")
                    }
                }
//                Row {
//                    Button(onClick = { mediaController.scanDeviceMusic() }) {
//                        Text("err-playback drop")
//                    }
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Button(onClick = { mediaController.simulateStreamFailure() }) {
//                        Text("err-simulate drop")
//                    }
                // -------------------
                // EXIT
                // -------------------
                Row {
                    Button(
                        onClick = {
                            controller.exit()
                            (context as? Activity)?.finish()
                        }
                    ) {
                        Text("Exit")
                    }
                }
                Row {
                    Button(onClick = { controller.simulatePlaybackDrop() }) {
                        Text("err-playback drop")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { controller.simulateStreamFailure() }) {
                        Text("err-simulate drop")
                    }
                }

            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }
}
