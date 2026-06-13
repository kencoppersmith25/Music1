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
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import android.util.Log
import com.example.music1.RadioController

class MainActivity : ComponentActivity() {

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        player = ExoPlayer.Builder(this).build()

        setContent {

            // ✅ SINGLE SOURCE OF TRUTH
            val controller = remember {
                RadioController(
                    player = player,
                    catalog = RadioCatalog
                )
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
                        if (controller.currentModeName.isNotEmpty()) {
                            Text("Mode: ${controller.currentModeName}")
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (controller.currentStationName.isNotEmpty()) {
                            Text("Station: ${controller.currentStationName}")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // -------------------
                        // MODE CONTROLS
                        // -------------------
                        Row {
                            Button(onClick = { controller.previousMode() }) {
                                Text("Prev Mode")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(onClick = { controller.nextMode() }) {
                                Text("Next Mode")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // -------------------
                        // STATION CONTROLS
                        // -------------------
                        Row {
                            Button(onClick = { controller.previousStation() }) {
                                Text("Prev Station")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = { controller.nextStation() }) {
                                Text("Next Station")
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // -------------------
                        // PLAY CONTROLS
                        // -------------------
                        Row {
                            Button(onClick = { controller.play() }) {
                                Text("Play")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(onClick = { controller.stop() }) {
                                Text("Stop")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = {
                                Log.d("kencheck", "BUTTON CLICK")
                                controller.playTestStream()
                            })
                            {
                                Text("TEST SOUND")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}