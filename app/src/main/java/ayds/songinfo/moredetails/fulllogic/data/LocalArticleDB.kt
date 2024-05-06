package ayds.songinfo.moredetails.fulllogic.data

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.fulllogic.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.domain.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.presentation.OtherInfoWindow

class LocalArticleDB {

    private val ARTICLE_DB_NAME = "database-article"
    private lateinit var articleDB: ArticleDatabase

    fun initLocalDB(context: Context) {
        articleDB =
            databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_DB_NAME).build()
    }

    fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        articleDB.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }

    fun getArticleFromDB(artistName: String): ArtistBiography? {
        val artistEntity = articleDB.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }
}