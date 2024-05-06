package ayds.songinfo.moredetails.fulllogic.domain

import android.content.Context

interface OtherInfoDataRepository {

    //ADD ALL LOGIC METHODS FOR ACCESS TO DATA AND QUERYS

    fun initDB(context: Context)
    fun getArtistInfoFromRepo(artistName:String): ArtistBiography
}
