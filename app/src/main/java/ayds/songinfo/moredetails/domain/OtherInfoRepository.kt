package ayds.songinfo.moredetails.domain

import android.content.Context

interface OtherInfoRepository {

    fun getArtistInfo(artistName:String): ArtistBiography
}
