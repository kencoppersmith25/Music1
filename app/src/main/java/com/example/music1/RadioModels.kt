package com.example.music1
data class Station(
    val name: String,
    val streamUrl: String
)
data class Mode(
    val name: String,
    val stations: List<Station>
)
