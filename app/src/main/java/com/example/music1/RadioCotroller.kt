package com.example.music1

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import android.content.Context
import androidx.media3.session.MediaSession
import android.support.v4.media.session.MediaSessionCompat
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.dp
import com.example.music1.RadioCommand
import com.example.music1.ui.theme.UserScreen

class RadioController(
    private val context: Context,
    private val player: ExoPlayer,
    private val catalog: RadioCatalog
){
    private val KEY_MODE = "modeIndex"
    private val KEY_STATION = "stationIndex"
    private val prefs = context.getSharedPreferences("radio_state", Context.MODE_PRIVATE)
    private val _modeIndex = MutableStateFlow(0)
    val modeIndex: StateFlow<Int> = _modeIndex.asStateFlow()
    private val _stationIndex = MutableStateFlow(0)
    val stationIndex: StateFlow<Int> = _stationIndex.asStateFlow()
    private val _currentModeName = MutableStateFlow("")
    val currentModeName: StateFlow<String> = _currentModeName.asStateFlow()
    private val _currentStationName = MutableStateFlow("")
    val currentStationName: StateFlow<String> = _currentStationName.asStateFlow()
    private val _currentGenreName = MutableStateFlow("")
    val currentGenreName: StateFlow<String> = _currentGenreName.asStateFlow()
    private val _playbackStatus = MutableStateFlow("Stopped")
    val playbackStatus: StateFlow<String> = _playbackStatus.asStateFlow()
    private val _isRecovering = MutableStateFlow(false)
    val isRecovering: StateFlow<Boolean> = _isRecovering.asStateFlow()
    private val _recoveryAttempts = MutableStateFlow(0)
    val recoveryAttempts: StateFlow<Int> = _recoveryAttempts.asStateFlow()
    private val _lastBluetoothEvent = MutableStateFlow("None")
    val lastBluetoothEvent: StateFlow<String> = _lastBluetoothEvent.asStateFlow()
    private val _bluetoothEventCount = MutableStateFlow(0)
    val bluetoothEventCount: StateFlow<Int> = _bluetoothEventCount.asStateFlow()
    private val _activeGenre = MutableStateFlow<String?>(null)
    val activeGenre: StateFlow<String?> = _activeGenre.asStateFlow()
    var maxRecoveryAttempts = 3
    private lateinit var mediaSession: MediaSession
    private var lastEventTime = 0L


    init {
        if (catalog.modes.isEmpty()) {
            _modeIndex.value = 0
            _stationIndex.value = 0
            _currentModeName.value = ""
            _currentStationName.value = ""
            _currentGenreName.value = ""

        } else {
            val safeModeIndex = prefs.getInt(KEY_MODE, 0).coerceIn(0, catalog.modes.lastIndex)
            _modeIndex.value = safeModeIndex
            val mode = catalog.modes[safeModeIndex]
            val safeStationIndex = prefs.getInt(KEY_STATION, 0)
                .coerceIn(0, mode.stations.lastIndex)
            _stationIndex.value = safeStationIndex
            _currentModeName.value = mode.name
            _currentStationName.value = mode.stations[safeStationIndex].name
            _currentGenreName.value = mode.stations[safeStationIndex].genre

        }

        fun mapKeyCodeToCommand(keyCode: Int): RadioCommand {
            Log.d("kencheck", "keyCode=$keyCode")
            return when (keyCode) {
                // modern Android media keys
                KeyEvent.KEYCODE_MEDIA_NEXT -> RadioCommand.NEXT_STATION
                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> RadioCommand.PREVIOUS_STATION
                KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
                KeyEvent.KEYCODE_HEADSETHOOK -> RadioCommand.TOGGLE_PLAYBACK
                // YOUR EARBUD RAW CODES (important)
                87 -> RadioCommand.NEXT_STATION
                88 -> RadioCommand.PREVIOUS_STATION
                126, 127 -> RadioCommand.TOGGLE_PLAYBACK

                else -> RadioCommand.UNKNOWN
            }
        }
        player.addListener(object : Player.Listener {

            override fun onPlayerError(error: PlaybackException) {
                _playbackStatus.value = "Error"
            }

        })
        mediaSession = MediaSession.Builder(context, player)
            .setCallback(object : MediaSession.Callback {

                override fun onMediaButtonEvent(
                    session: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    intent: Intent
                ): Boolean {

                    val event = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)

                    Log.d("kencheck", "MEDIA BUTTON EVENT: $event")

                    if (event != null && event.action == KeyEvent.ACTION_DOWN) {
                        val command = mapKeyCodeToCommand(event.keyCode)
                        Log.e("kencheck", "MEDIA BUTTON CALLBACK ENTERED")
                        onCommand(command)
                    }

                    return true
                }
            })
            .build()
    }
    fun onCommand(command: RadioCommand) {
        when (command) {
            RadioCommand.NEXT_STATION -> nextStation()
            RadioCommand.PREVIOUS_STATION -> prevStation()
            RadioCommand.TOGGLE_PLAYBACK -> togglePlayStop()
            RadioCommand.UNKNOWN -> {}
        }
    }
    val currentMode: Mode
        get() = catalog.modes.getOrElse(_modeIndex.value){
            catalog.modes.first()
        }
    val currentStation: Station
        get() = currentMode.stations.getOrElse(_stationIndex.value){
            currentMode.stations.first()
        }
    fun nextStation(){
        updateStationIndex(_stationIndex.value+1)
    }
    fun prevStation(){
        updateStationIndex(_stationIndex.value-1)
    }
    fun Station.toMediaItem():MediaItem{
        return MediaItem.fromUri(this.streamUrl)
    }
    fun nextRandomStation(){
        val stations=currentMode.stations
        if(stations.size<=1)return
        val step=(1 until stations.size).random()
        _stationIndex.value=(_stationIndex.value+step)%stations.size
        updateStationIndex(_stationIndex.value)
    }
    private fun updateStationIndex(newIndex: Int) {
        val mode = catalog.modes[_modeIndex.value]
        val size = mode.stations.size
        val finalIndex = (newIndex + size) % size
        val station = mode.stations[finalIndex]
        _stationIndex.value = finalIndex
        _currentStationName.value = station.name
        _currentGenreName.value = station.genre
        saveState()
        playStation(station)
    }
    fun setMode(mode:Mode){
        _currentModeName.value=mode.name
    }
    fun setStation(station:Station){
        _currentStationName.value=station.name
    }
    fun setGenre(station:Station){
        _currentGenreName.value=station.genre
    }
    private fun saveState(){
        prefs.edit()
            .putInt(KEY_MODE,_modeIndex.value)
            .putInt(KEY_STATION,_stationIndex.value)
            .apply()
    }
    fun togglePlayStop(){
        if (_playbackStatus.value in listOf("idle","Stopped")) {
            play()
        } else {
            stop()
        }
    }
   fun simulateStreamFailure(){
        Log.e("kencheck","SIMULATED STREAM FAILURE")
        _playbackStatus.value="Error"
        recoverStream()
    }
    fun simulatePlaybackDrop(){
        Log.e("kencheck","SIMULATED PLAYBACK DROP")
        player.stop()
        recoverStream()
    }
    private fun recoverStream(){
        if(_isRecovering.value)return
        if(_recoveryAttempts.value>=maxRecoveryAttempts){
            _playbackStatus.value="Failed"
            return
        }
        _isRecovering.value=true
        _recoveryAttempts.value++
        _playbackStatus.value="Reconnecting(${_recoveryAttempts.value})"
        player.stop()
        val delay=1000L*_recoveryAttempts.value
        Handler(Looper.getMainLooper()).postDelayed({
            try{
                val item=MediaItem.fromUri(currentStation.streamUrl)
                player.setMediaItem(item)
                player.prepare()
                player.playWhenReady=true
                player.play()
                _playbackStatus.value="Playing"
            }catch(e:Exception){
                Log.e("kencheck","Recovery failed",e)
                _playbackStatus.value="Error"
            }finally{
                _isRecovering.value=false
            }
        },delay)
    }
    fun nextMode() {
        val modeCount = catalog.modes.size
        val newModeIndex = (_modeIndex.value + 1) % modeCount
        val mode = catalog.modes[newModeIndex]
        val station = mode.stations.first()
        _modeIndex.value = newModeIndex
        _stationIndex.value = 0
        _currentModeName.value = mode.name
        _currentStationName.value = station.name
        _currentGenreName.value = station.genre
        saveState()
        playStation(station)
    }
    fun previousMode(){
        val modeCount = catalog.modes.size
        val newModeIndex = (_modeIndex.value - 1 + modeCount) % modeCount
        val mode = catalog.modes[newModeIndex]
        val station = mode.stations.first()
        _modeIndex.value = newModeIndex
        _stationIndex.value = 0
        _currentModeName.value = mode.name
        _currentStationName.value = station.name
        _currentGenreName.value = station.genre
        saveState()
        playStation(station)
    }
    fun play(){
        val stations=currentMode.stations
        if(stations.isEmpty())return
        val station=currentStation
        val item=MediaItem.fromUri(station.streamUrl)
        player.setMediaItem(item)
        player.prepare()
        player.playWhenReady=true
        player.play()
        _playbackStatus.value = "Playing"
    }
    private fun playStation(station: Station) {
        val item = MediaItem.fromUri(station.streamUrl)
        player.setMediaItem(item)
        player.prepare()
        player.playWhenReady = true
        player.play()
        _playbackStatus.value = "Playing"
    }
    fun exit(){
        player.stop()
        player.release()
        mediaSession.release()
    }
    fun stop(){
        player.stop()
        _playbackStatus.value = "idle"
    }
    /////////////////////////////////////////////////////////
    ////////         new station stuff             //////////
    // ///////////////////////////////////////////////////////

    private fun filteredStations(): List<Station> {
        val genre = _activeGenre.value
        val allStations = catalog.modes.flatMap { it.stations }
        return if (genre == null) {
            allStations
        } else {
            allStations.filter { it.genre == genre }
        }
    }
    fun clearGenreFilter() {
        _activeGenre.value = null
    }
}