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
    private val artistBiography:ArtistBiography = mockk(relaxUnitFun = true)

    @Test
    fun `Given an artist that is locally stored result should contain special character`(){
        every {
            artistBiography.isLocallyStored
        } returns true
        every {
            artistBiography.biography
        } returns "Biography"
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
        every {
            artistBiography.isLocallyStored
        } returns false
        every {
            artistBiography.biography
        } returns "Biography"
        val builder = StringBuilder()
        builder.append(HEADER)
        builder.append(artistBiography.biography)
        builder.append(FOOTER)
        val expectedResult = builder.toString()

        val actualResult = artistBiographyDescriptionHelper.getDescription(artistBiography)
        Assert.assertEquals(expectedResult, actualResult)
    }
}