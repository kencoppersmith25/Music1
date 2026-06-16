package com.example.music1

object RadioCatalog {

    val modes = listOf(
        Mode(
            name = "Tone Test mode 1", stations = listOf(
                Station("Sine 200Hz 1-1","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
                Station("Sine 400Hz 1-2","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
                Station("Sine 600Hz 1-3","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"))),
        Mode(
            name = "Music Test mode 2", stations = listOf(
                Station("Track A - Calm 2-1","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"),
                Station("Track B - Beat 2-2","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"),
                Station("Track C - Fast 2-3","https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3"))),
        Mode(
            name = "Voice Test mode 3", stations = listOf(
                Station("Voice 3-1", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3"),
                Station("Voice 3-2", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3"),
                Station("Voice 3-3", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3")
            )
        )
    )
}
//        Mode("Classical 1", listOf(
//            Station("Classical 1","https://ice1.somafm.com/dronezone-128-mp3"),
//            Station("classical 2","https://ice1.somafm.com/lush-128-mp3"),
//            Station("classical 3","https://ice1.somafm.com/lush-128-mp3"))),
//        Mode("Latin 2", listOf(
//            Station("latin 1","https://ice1.somafm.com/groovesalad-128-mp3"),
//            Station("latin 2","https://ice1.somafm.com/beatblender-128-mp3"),
//            Station("latin 3","https://ice1.somafm.com/lush-128-mp3"))),
//        Mode("Hip Hop 3", listOf(
//            Station("hh 1","https://ice1.somafm.com/suburbsofgoa-128-mp3"),
//            Station("hh 2","https://ice1.somafm.com/groovesalad-128-mp3"),
//            Station("hh 3","https://ice1.somafm.com/suburbsofgoa-128-mp3")))
//    )
