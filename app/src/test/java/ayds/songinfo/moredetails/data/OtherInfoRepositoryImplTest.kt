package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.ExternalService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test


class OtherInfoRepositoryImplTest{

    private val externalService: ExternalService = mockk(relaxUnitFun = true)
    private val localStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)

    private val otherInfoRepository: OtherInfoRepository =
        OtherInfoRepositoryImpl(externalService, localStorage)

    @Test
    fun `given an existing artist name in database should return corresponding biography`() {
        val biography: ArtistBiography = mockk()
        every { localStorage.getArticle("artistName") } returns biography

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(biography, result)
    }

    @Test
    fun `given an non existing artist name in database should retrieve corresponding biography from external service`() {
        val biography: ArtistBiography = mockk()
        every { localStorage.getArticle("artistName") } returns null
        every { externalService.getArticle("artistName") } returns biography

        val result = otherInfoRepository.getArtistInfo("artistName")

        Assert.assertEquals(biography, result)
    }

    //Como hacer un test en base a guardar la biografia si NO es un dato vacio externo? el ultiumo path es una accion que modifica BD
    //@Test
    //fun `given an artist biography that was retrieved from external service `() {
    //    val biography: ArtistBiography = mockk()
    //    every { localStorage.getArticle("artistName") } returns biography
    //
    //    val result = otherInfoRepository.getArtistInfo("artistName")
    //
    //    Assert.assertEquals(biography, result)
    //}
}