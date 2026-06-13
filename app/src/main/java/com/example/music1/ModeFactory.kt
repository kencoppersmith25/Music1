package com.example.music1

class ModeFactory {

    fun buildModeFromTag(
        name: String,
        tag: String,
        shuffle: Boolean = false
    ): Mode {
        return Mode(
            name = name,
            includeTags = listOf(tag),
            shuffle = shuffle
        )
    }

    fun buildModeFromTags(
        name: String,
        tags: List<String>,
        shuffle: Boolean = false
    ): Mode {
        return Mode(
            name = name,
            includeTags = tags,
            shuffle = shuffle
        )
    }
}