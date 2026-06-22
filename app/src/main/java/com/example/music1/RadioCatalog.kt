package com.example.music1

object RadioCatalog {
    var modes=listOf(
        Mode(name="Tone Test mode 1",
            stations=listOf(
                Station(id=1,name="Sine 200Hz 1-1",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
                Station(id=2,name="Sine 400Hz 1-2",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
                Station(id=3,name="Sine 600Hz 1-3",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"))),
        Mode(name="Music Test mode 2",
            stations=listOf(
                Station(id=4,name="Track A - Calm 2-1",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"),
                Station(id=5,name="Track B - Beat 2-2",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"),
                Station(id=6,name="Track C - Fast 2-3",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3"))),
        Mode(name="Voice Test mode 3",
            stations=listOf(
                Station(id=7,name="Voice 3-1",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3"),
                Station(id=8,name="Voice 3-2",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3"),
                Station(id=9,name="Voice 3-3",type=SourceType.STREAM,location="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3")))
    )
    fun search(query: String): List<Station> {
        val q = query.lowercase().trim()
        return modes
            .flatMap { it.stations }
            .filter { station ->
                station.name.lowercase().contains(q)
            }
    }


}

data class RadioCategory(
    val id: String,
    val name: String,
    val keywords: List<String>
)
private val categories = listOf(
    RadioCategory(
        id = "christian_rap",
        name = "Christian Rap",
        keywords = listOf("christian rap", "holy hip hop", "gospel rap")
    )
)
