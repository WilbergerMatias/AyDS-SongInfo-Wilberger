package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.ExternalService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private var externalService: ExternalService,
    private var localStorage: OtherInfoLocalStorage
    ): OtherInfoRepository {

    override fun getArtistInfo(artistName:String): ArtistBiography {
        val article = localStorage.getArticle(artistName)
        val artistBiography: ArtistBiography

        if (article != null) {
            artistBiography = article.apply {markItAsLocal()}
        } else {
            artistBiography = externalService.getArticle(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                localStorage.insertArtist(artistBiography)
            }
        }
        return artistBiography
    }

    private fun ArtistBiography.markItAsLocal(){
        isLocallyStored = true
    }
}

