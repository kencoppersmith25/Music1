package com.example.music1

object RadioCatalog {

    val modes = listOf(
        Mode("Classical", listOf(
            Station("Classical 1","https://ice1.somafm.com/dronezone-128-mp3"),
            Station("classical 2","https://ice1.somafm.com/lush-128-mp3"),
            Station("classical 3","https://ice1.somafm.com/lush-128-mp3"))),
        Mode("Latin", listOf(
            Station("latin 1","https://ice1.somafm.com/groovesalad-128-mp3"),
            Station("latin 2","https://ice1.somafm.com/beatblender-128-mp3"),
            Station("latin 3","https://ice1.somafm.com/lush-128-mp3"))),
        Mode("Hip Hop", listOf(
            Station("hh 1","https://ice1.somafm.com/suburbsofgoa-128-mp3"),
            Station("hh 2","https://ice1.somafm.com/groovesalad-128-mp3"),
            Station("hh 3","https://ice1.somafm.com/suburbsofgoa-128-mp3")))
    )
}