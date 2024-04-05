package ayds.songinfo.home.model

import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong

interface SongDateHelper {
    fun getSongReleaseDateText(song: Song = Song.EmptySong): String
}

    private const val MONTH = "month"
    private const val YEAR = "year"
    internal class SongDateHelperImpl : SongDateHelper {

        override fun getSongReleaseDateText(song: Song): String {
            return when (song) {
                is Song.SpotifySong ->
                    this.getDateText(song)
                else -> "Date not found"
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



