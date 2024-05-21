package ayds.songinfo.home.view.songDescription

import ayds.songinfo.home.model.entities.Song.SpotifySong

private const val MONTH = "month"
private const val YEAR = "year"
private const val DAY = "day"
internal var year = ""
internal var month = ""
internal var day = ""
internal var date = listOf("Year", "Month", "Day")

interface SongDateHelper {
    val song: SpotifySong
    fun getSongReleaseDateText(): String
}

interface ReleaseDateHelperFactory{
    fun getHelper(song:SpotifySong):SongDateHelper
}
internal class ReleaseDateHelperFactoryImpl: ReleaseDateHelperFactory{
    override fun getHelper(song:SpotifySong): SongDateHelper =
        when (song.releaseDatePrecision){
            YEAR -> SongDateHelperYearImpl(song)
            MONTH -> SongDateHelperMonthImpl(song)
            DAY -> SongDateHelperDayImpl(song)
            else -> SongDateHelperDefaultImpl(song)
        }
    }

internal class SongDateHelperDefaultImpl(override val song: SpotifySong): SongDateHelper {
    override fun getSongReleaseDateText(): String {
        return song.releaseDate
    }

}
    //sealed class  SongDate{}

internal class SongDateHelperDayImpl(override val song: SpotifySong) :SongDateHelper {
        override fun getSongReleaseDateText(): String {
            date = song.releaseDate.split("-")
            year = date[0]
            month = date[1]
            day = date[2]
            return "$day/$month/$year"
        }
    }

internal class SongDateHelperMonthImpl(override val song: SpotifySong) :SongDateHelper {
    private val monthNameMap = mapOf(
        "1" to "January",
        "2" to "February",
        "3" to "March",
        "4" to "April",
        "5" to "May",
        "6" to "June",
        "7" to "July",
        "8" to "August",
        "9" to "September",
        "10" to "October",
        "11" to "November",
        "12" to "December"
    )
    override fun getSongReleaseDateText(): String {
        date = song.releaseDate.split("-")
        year = date.first()
        month = monthNameMap.getValue(date[1])
        return "$month,$year"
    }
}

internal class SongDateHelperYearImpl(override val song: SpotifySong) :SongDateHelper {
    override fun getSongReleaseDateText(): String {
        date = song.releaseDate.split("-")
        year = date.first()
        val suffix = if(isLeapYear(year.toInt())) "(leap year)" else "(not leap year)"
        return "$year $suffix"
    }
    private fun isLeapYear(y:Int) = (y%400==0) || (y%100 !=0 && y%4==0)
}



