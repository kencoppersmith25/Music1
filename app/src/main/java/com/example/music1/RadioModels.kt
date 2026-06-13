package com.example.music1
data class Station(
    val name: String,
    val streamUrl: String,
    val tags: List<String>

)

data class Mode(
    val name: String,
    val includeTags: List<String>,
    val shuffle: Boolean = false
)
