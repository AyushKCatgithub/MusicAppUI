package eu.tutorials.musicappui

import androidx.annotation.DrawableRes

data class LibDummy(@DrawableRes val drawable: Int, val title: String)

val libraries = listOf<LibDummy>(
    LibDummy(R.drawable.baseline_queue_music_25, "Playlist"),
    LibDummy(R.drawable.baseline_mic_25,"Artists"),
    LibDummy(R.drawable.baseline_album_25,"Albums"),
    LibDummy(R.drawable.baseline_music_note_25,"Songs"),
    LibDummy(R.drawable.baseline_mic_external_on_25,"Genre")
)


