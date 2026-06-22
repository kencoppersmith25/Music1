package com.example.music1

data class Station(
    val id: Int,
    val name: String,
    val type: SourceType,
    val location: String // URL for streams
)
data class Mode(
    val name: String,
    val stations: List<Station>
)

enum class RadioCommand {
    NEXT_STATION,
    PREVIOUS_STATION,
    TOGGLE_PLAYBACK,
    UNKNOWN
}
enum class SourceType {
    STREAM,
    FOLDER,
    PLAYLIST,
    FILE
}

