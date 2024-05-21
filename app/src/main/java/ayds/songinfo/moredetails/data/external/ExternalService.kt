package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.ArtistBiography
import java.io.IOException

interface ExternalService {
    fun getArticle(artistName: String):ArtistBiography
}

internal class ExternalServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToArtist:LastFMToArtistBiographyResolver
):ExternalService {

    // API call and parsing
    override fun getArticle(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSong(artistName)
            artistBiography = lastFMToArtist.map(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return artistBiography
    }
    private fun getSong(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()
}