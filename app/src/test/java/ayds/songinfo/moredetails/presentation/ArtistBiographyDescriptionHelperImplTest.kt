package ayds.songinfo.moredetails.presentation

import ayds.songinfo.home.view.HomeUiState
import ayds.songinfo.moredetails.domain.ArtistBiography
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ArtistBiographyDescriptionHelperImplTest{

    private val HEADER ="<html><div width=400><font face=\"arial\">"
    private val FOOTER = "</font></div></html>"
    private val LOCALLYSTORED = "[*]"

    private val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()

    @Test
    fun `Given an artist that is locally stored result should contain special character`(){
        val artistBiography = ArtistBiography("artistName", "biography", "url", true)
        val builder = StringBuilder()
        builder.append(LOCALLYSTORED)
        builder.append(HEADER)
        builder.append(artistBiography.biography)
        builder.append(FOOTER)
        val expectedResult = builder.toString()

        val actualResult = artistBiographyDescriptionHelper.getDescription(artistBiography)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given an artist that is not locally stored result should not contain special character`(){
        val artistBiography = ArtistBiography("artistName", "biography", "url", false)
        val builder = StringBuilder()
        builder.append(HEADER)
        builder.append(artistBiography.biography)
        builder.append(FOOTER)
        val expectedResult = builder.toString()

        val actualResult = artistBiographyDescriptionHelper.getDescription(artistBiography)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should remove apostrophes`() {
        val artistBiography = ArtistBiography("artist", "biography'n", "url", false)

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        Assert.assertEquals(
            "<html><div width=400><font face=\"arial\">biography n</font></div></html>",
            result
        )
    }

    @Test
    fun `should fix on double slash`() {
        val artistBiography = ArtistBiography("artist", "biography\\n", "url", false)

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        Assert.assertEquals(
            "<html><div width=400><font face=\"arial\">biography<br></font></div></html>",
            result
        )
    }

    @Test
    fun `should map break lines`() {
        val artistBiography = ArtistBiography("artist", "biography\n", "url", false)

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        Assert.assertEquals(
            "<html><div width=400><font face=\"arial\">biography<br></font></div></html>",
            result
        )
    }
    @Test
    fun `should set artist name bold`() {
        val artistBiography = ArtistBiography("artist", "biography artist", "url", false)

        val result = artistBiographyDescriptionHelper.getDescription(artistBiography)

        Assert.assertEquals(
            "<html><div width=400><font face=\"arial\">biography <b>ARTIST</b></font></div></html>",
            result
        )
    }
}