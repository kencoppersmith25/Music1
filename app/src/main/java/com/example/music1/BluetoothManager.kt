package com.example.music1

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager as SystemBluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.util.Log
import android.media.AudioManager

class BluetoothManager(private val context: Context) {
    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    fun isBluetoothAudioOn(): Boolean {
        return audioManager.isBluetoothA2dpOn
    }
}
