package ayds.songinfo.home.view

import ayds.songinfo.home.model.SongDateHelper
import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import ayds.songinfo.utils.SongDescriptionInjector

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {

    private var songDateHelper: SongDateHelper = SongDescriptionInjector.songDateHelper
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Date: ${songDateHelper.getSongReleaseDateText(song)}"
            else -> "Song not found"
        }
    }
}