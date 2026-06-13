package com.example.music1

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RadioController(
    private val player: ExoPlayer,
    private val catalog: RadioCatalog,

) {

    private val modes = catalog.modes

    var modeIndex by androidx.compose.runtime.mutableStateOf(0)
    var stationIndex by androidx.compose.runtime.mutableStateOf(0)
    var currentModeName by mutableStateOf("")
    var currentStationName by mutableStateOf("")

    val currentMode: Mode
        get() = modes[modeIndex]

    fun playTestStream() {
        player.setMediaItem(
            MediaItem.fromUri("https://ice1.somafm.com/dronezone-128-mp3")
        )
        player.prepare()
        player.play()
    }
    val currentStation: Station
        get() = currentMode.stations[stationIndex]

    init {
        // Add listener ONCE (prevents stacking listeners)
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e("kencheck", "Playback error", error)
            }
        })
    }

    fun nextStation() {
        val stations = currentMode.stations
        if (stations.isEmpty()) return
        stationIndex = (stationIndex + 1) % stations.size
        Log.e("kencheck","next station hit - index: $stationIndex  name: ${currentStation.name}")
        play()
    }
    fun nextRandomStation() {
        val stations = currentMode.stations
        if (stations.size <= 1) return
        var step = (1..(stations.size-1)).random()
        stationIndex = (stationIndex + step) % stations.size
        Log.e("kencheck", "next station hit - index: ${stationIndex}  name: ${currentStation.name}")
        play()
    }

    fun previousStation() {
        val stations = currentMode.stations
        if (stations.isEmpty()) return
        stationIndex = (stationIndex - 1 + stations.size) % stations.size
        Log.e("kencheck", "previous station hit - index: ${stationIndex}  name: ${currentStation.name}")
        play()
    }

    fun nextMode() {
        modeIndex = (modeIndex + 1) % modes.size
        Log.e("kencheck", "next mode hit - index: ${modeIndex}  name: ${currentModeName}")
        stationIndex = 0
        play()
    }

    fun previousMode() {
        modeIndex = (modeIndex - 1 + modes.size) % modes.size
        stationIndex = 0
        Log.e("kencheck", "previous mode hit - index: ${modeIndex}  name: ${currentModeName}")
        play()
    }

    fun play() {
        val stations = currentMode.stations
        if (stations.isEmpty()) {
            Log.e("kencheck", "play - No stations found for mode ${currentMode.name}")
            return
        }
        val station = stations[stationIndex]
        currentModeName = currentMode.name
        currentStationName = station.name
        Log.d("kencheck","play - Mode=${currentMode.name} stationsFound=${currentMode.stations.size}")
        Log.d("kencheck", "play - playing current mode/station :  stationindex: ${stationIndex}  stationname: ${currentStation.name} modeindex: ${modeIndex}  modename: ${currentModeName}")
        val item = MediaItem.fromUri(station.streamUrl)
        player.setMediaItem(item)
        player.prepare()
        player.playWhenReady = true
        player.play()
    }

    fun stop() {
        Log.d("kencheck", "Stopped")
        player.stop()
    }
}