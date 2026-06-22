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
import android.view.KeyEvent

class MainActivity : ComponentActivity() {
    private lateinit var controller: RadioController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val player = ExoPlayer.Builder(this).build()
        val musicRepository = MusicRepository(this)
        controller = RadioController(this,player)
        Log.e("kencheck", "**************APP STARTED pid=${android.os.Process.myPid()}")
        setContent {
            UserScreen(controller)
        }
    }
//    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        val keyCode = event.keyCode
//        Log.d("kencheck", "keyCode=$keyCode")
//        if (::controller.isInitialized && event.action == KeyEvent.ACTION_DOWN) {
//            controller.onCommand(
//                controller.mapKeyCodeToCommand(keyCode)
//            )
//            )
//        }
//        return super.dispatchKeyEvent(event)
//    }
}