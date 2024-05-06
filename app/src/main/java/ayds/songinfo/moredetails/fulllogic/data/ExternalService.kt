package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.domain.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.LastFMAPI
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

class ExternalService {

    private val AUDIOSCROBBLER_URL = "https://ws.audioscrobbler.com/2.0/"
    private lateinit var lastFMAPIService: LastFMAPI


    fun initExternalService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIOSCROBBLER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPIService = retrofit.create(LastFMAPI::class.java)
    }

    // API call and parsing
    fun getArticleFromService(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }
    private fun getSongFromService(artistName: String) =
        lastFMAPIService.getArtistInfo(artistName).execute()

    private fun getArtistBioFromExternalData(serviceData: String?,artistName: String): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }
}