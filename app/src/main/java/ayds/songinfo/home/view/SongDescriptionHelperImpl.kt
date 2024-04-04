package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

private const val MONTH = "month"
private const val YEAR = "year"
internal class SongDescriptionHelperImpl : SongDescriptionHelper {


    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Date: ${this.getDateText(song)}"
            else -> "Song not found"
        }
    }

    private fun getDateText(song: SpotifySong): String {
        val precision = song.releaseDatePrecision

        return when (precision) {
            YEAR -> song.releaseDate.split("-").first()
            MONTH -> song.releaseDate.split("-").subList(0,1).toString()
            else -> song.releaseDate
        }
    }
}