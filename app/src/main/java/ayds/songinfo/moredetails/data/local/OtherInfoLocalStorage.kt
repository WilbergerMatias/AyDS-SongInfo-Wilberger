package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.ArtistBiography

interface OtherInfoLocalStorage{

    fun insertArtist(artistBiography: ArtistBiography)
    fun getArticle(artistName: String): ArtistBiography?
}

internal class OtherInfoLocalStorageImpl(
    private val articleDatabase: ArticleDatabase,
) : OtherInfoLocalStorage {

    override fun insertArtist(artistBiography: ArtistBiography) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }

    override fun getArticle(artistName: String): ArtistBiography? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }
}