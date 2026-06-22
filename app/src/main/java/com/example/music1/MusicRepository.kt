package com.example.music1

import android.content.Context
import android.provider.MediaStore
import android.content.ContentUris

class MusicRepository(private val context: Context) {
    fun scanDeviceMusic(): List<Station> {
        val stations = mutableListOf<Station>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME
        )
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val contentUri = ContentUris.withAppendedId(uri, id)
                stations.add(
                    Station(
                        id = id.toInt(),
                        name = name,
                        type = SourceType.FILE,
                        location = contentUri.toString()
                    )
                )
            }
        }
        return stations
    }
}
fun scanDeviceMusic(context: Context): List<Station> {
    val stations = mutableListOf<Station>()
   val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME
    )
    context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val contentUri = ContentUris.withAppendedId(uri, id)
            stations.add(
                Station(
                    id = id.toInt(),
                    name = name,
                    type = SourceType.FILE,
                    location = contentUri.toString()
                )
            )
        }
    }
    return stations
}