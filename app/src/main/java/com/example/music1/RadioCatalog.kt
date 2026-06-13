package com.example.music1

object RadioCatalog {

    val stations = listOf(
        Station(
            "Classical Oasis",
            "https://ice1.somafm.com/dronezone-128-mp3",
            listOf("classical")
        ),
        Station(
            "KWMU",
            "https://ice1.somafm.com/lush-128-mp3",
            listOf("classical")
        ),
        Station(
            "Habana Club Radio",
            "https://ice1.somafm.com/groovesalad-128-mp3",
            listOf("latin")
        ),
        Station(
            "Puerto Rico Radio\n",
            "https://ice1.somafm.com/beatblender-128-mp3",
            listOf("latin")
        ),
        Station(
            "Urban Radio Hip-Hop & R&B",
            "https://ice1.somafm.com/suburbsofgoa-128-mp3",
            listOf("Hip Hop")
        ),
        Station(
            "Boost Radio",
            "https://ice1.somafm.com/groovesalad-128-mp3",
            listOf("Hip Hop")
        )
    )
    val modes = listOf(
        Mode("Classical", listOf("classical")),
        Mode("Latin", listOf("latin")),
        Mode("Hip Hop", listOf("hip hop"))
    )
}