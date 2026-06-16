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
import android.content.Intent
import android.view.KeyEvent
import android.os.Handler
import android.os.Looper

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
    var maxRecoveryAttempts = 3
    init{
        if(catalog.modes.isEmpty()){
            _modeIndex.value=0
            _stationIndex.value=0
            _currentModeName.value=""
            _currentStationName.value=""
        }else{
            val safeModeIndex=prefs.getInt(KEY_MODE,0).coerceIn(0,catalog.modes.lastIndex)
            _modeIndex.value=safeModeIndex
            val mode=catalog.modes[safeModeIndex]
            val safeStationIndex=prefs.getInt(KEY_STATION,0)
                .coerceIn(0,mode.stations.lastIndex)
            _stationIndex.value=safeStationIndex
            _currentModeName.value=mode.name
            _currentStationName.value=mode.stations[safeStationIndex].name
        }
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                _playbackStatus.value = when(state) {
                    Player.STATE_BUFFERING -> "Buffering"
                    Player.STATE_READY -> "Playing"
                    Player.STATE_ENDED -> "Idle"
                    else -> "Idle"
                }
            }
            override fun onPlayerError(error: PlaybackException) {
                _playbackStatus.value = "Error"
            }
        })
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
        saveState()
        playStation(station)
    }
    fun setMode(mode:Mode){
        _currentModeName.value=mode.name
    }
    fun setStation(station:Station){
        _currentStationName.value=station.name
    }
    private fun saveState(){
        prefs.edit()
            .putInt(KEY_MODE,_modeIndex.value)
            .putInt(KEY_STATION,_stationIndex.value)
            .apply()
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
    }
    fun stop(){
        player.stop()
        _playbackStatus.value = "idle"
    }
}