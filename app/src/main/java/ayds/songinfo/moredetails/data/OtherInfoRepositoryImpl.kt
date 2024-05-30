package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.LastFmBiography
import ayds.artist.external.lastfm.LastFmService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private var externalService: OtherInfoLocalStorageImpl,
    private var localStorage: LastFmService
    ): OtherInfoRepository {

    override fun getCard(artistName:String): Card {
        val dbCard = localStorage.getCard(artistName)
        val card: Card

        if (dbCard != null) {
            card = dbCard.apply {markItAsLocal()}
        } else {
            card = externalService.getArticle(artistName).toCard()
            if (card.text.isNotEmpty()) {
                localStorage.insertCard(card)
            }
        }
        return card
    }

    private fun Card.markItAsLocal(){
        isLocallyStored = true
    }

    private fun LastFmBiography.toCard() = Card(artistName, biography, articleUrl, CardSource.LAST_FM)
}

