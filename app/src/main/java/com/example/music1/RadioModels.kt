package com.example.music1
data class Station(
    val name: String,
    val streamUrl: String,
    val genre: String
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
