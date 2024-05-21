package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.ExternalService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test


class OtherInfoRepositoryTest{

    private val externalService: ExternalService = mockk(relaxUnitFun = true)
    private val localStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)

    private val otherInfoRepository: OtherInfoRepository =
        OtherInfoRepositoryImpl(externalService, localStorage)

    @Test
    fun `given an existing artist name in database should return corresponding biography`() {
        val artistBiography = ArtistBiography("artistName","artistBiography","url",false)
        every { localStorage.getArticle("artistName") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(artistBiography, result)
        Assert.assertTrue(result.isLocallyStored)
    }

    @Test
    fun `given an non existing artist name in database should retrieve corresponding biography from external service`() {
        val artistBiography = ArtistBiography("artistName","artistBiography","url",false)
        every { localStorage.getArticle("artistName") } returns null
        every { externalService.getArticle("artistName") } returns artistBiography
        every { localStorage.insertArtist(artistBiography) } returns Unit

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(artistBiography, result)
        Assert.assertFalse(result.isLocallyStored)
        verify{localStorage.insertArtist(artistBiography)}
    }


    @Test
    fun `given an empty artist biography, getArtistInfo call getArticle from service`() {
        val artistBiography = ArtistBiography("artistName","artistBiography","url",false)
        every { localStorage.getArticle("artistName") } returns null
        every { externalService.getArticle("artistName") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(artistBiography, result)
        Assert.assertFalse(result.isLocallyStored)
        verify(inverse = true) { localStorage.insertArtist(artistBiography) }
    }
}
