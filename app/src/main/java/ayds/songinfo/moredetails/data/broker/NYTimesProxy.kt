package ayds.songinfo.moredetails.data.broker

import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class NYTimesProxy(
    private val nyTimesService:NYTimesService
):CardProxy {
    override fun getCard(artistName: String): Card? {
        val article = nyTimesService.getArtistInfo(artistName)
        if (article is NYTimesArticle.NYTimesArticleWithData){
            return article.toCard()
        }
        return null
    }

    private fun NYTimesArticle.toCard() =
        Card(name?:"", info?:"", url, NYT_LOGO_URL, CardSource.NY_TIMES)
}