package ayds.songinfo.moredetails.data


import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class OtherInfoRepositoryTest {

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk()
    private val otherInfoService: ayds.artist.external.external.OtherInfoService = mockk()
    private val otherInfoRepository: OtherInfoRepository = OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)

    @Test
    fun `on getArtistInfo call getArticle from local storage`() {
        val card = Card("artist", "biography", "url", false)
        every { otherInfoLocalStorage.getCard("artist") } returns card

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(card, result)
        Assert.assertTrue(result.isLocallyStored)
    }

    @Test
    fun `on getArtistInfo call getArticle from service`() {
        val card = Card("artist", "biography", "url", false)
        every { otherInfoLocalStorage.getCard("artist") } returns null
        every { otherInfoService.getArticle("artist") } returns card
        every { otherInfoLocalStorage.insertCard(card) } returns Unit

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(card, result)
        Assert.assertFalse(result.isLocallyStored)
        verify { otherInfoLocalStorage.insertCard(card) }
    }

    @Test
    fun `on empty bio, getArtistInfo call getArticle from service`() {
        val card = Card("artist", "", "url", false)
        every { otherInfoLocalStorage.getCard("artist") } returns null
        every { otherInfoService.getArticle("artist") } returns card

        val result = otherInfoRepository.getCard("artist")

        Assert.assertEquals(card, result)
        Assert.assertFalse(result.isLocallyStored)
        verify(inverse = true) { otherInfoLocalStorage.insertCard(card) }
    }
}
