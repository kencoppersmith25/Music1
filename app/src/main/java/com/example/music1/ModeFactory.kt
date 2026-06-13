package com.example.music1

import kotlin.String

class ModeFactory {

    fun buildModeFromTag(
        name: String,
        stations: List<Station>
    ): Mode {
        return Mode(
            name = name,
            stations = stations
        )
    }

    fun buildModeFromTags(
        name: String,
        stations: List<Station>,
    ): Mode {
        return Mode(
            name = name,
            stations = stations
        )
    }
}