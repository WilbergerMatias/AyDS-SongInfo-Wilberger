package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.LastFmBiography
import ayds.artist.external.lastfm.LastFmService
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.data.broker.OtherInfoBroker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository

class OtherInfoRepositoryImpl(
    private val localStorage: OtherInfoLocalStorage,
    private val otherInfoBroker:OtherInfoBroker
    ): OtherInfoRepository {

    override fun getCard(artistName:String): List<Card> {
        val dbCard = localStorage.getCard(artistName)

        if (dbCard.isNotEmpty()) {
            return dbCard.apply { markItAsLocal() }
        } else {
            val cards = otherInfoBroker.getCards(artistName)

            cards.forEach { localStorage.insertCard(it) }

            return cards
        }
    }

    private fun List<Card>.markItAsLocal(){
        this.forEach{it.isLocallyStored = true}
    }


}

