package ayds.songinfo.moredetails.fulllogic.data


import android.content.Context
import ayds.songinfo.moredetails.fulllogic.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.domain.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.domain.OtherInfoDataRepository
import ayds.songinfo.moredetails.fulllogic.domain.markItAsLocal
import android.content.Intent

class OtherInfoDataRepositoryImpl: OtherInfoDataRepository {

// IMPLEMENT METHODS (pass methods from this file to the interface, then override here)
    private lateinit var localDB:LocalArticleDB
    private lateinit var service:ExternalService
    //LOCAL
    private lateinit var articleDB: ArticleDatabase

    override fun initDB(context: Context){
        localDB.initLocalDB(context)
        service.initExternalService()
    }

    override fun getArtistInfoFromRepo(artistName:String):ArtistBiography{

        val dbArticle = localDB.getArticleFromDB(artistName)

        val artistBiography: ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.markItAsLocal()
        } else {
            artistBiography = service.getArticleFromService(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                localDB.insertArtistIntoDB(artistBiography)
            }
        }
        return artistBiography
    }


}

