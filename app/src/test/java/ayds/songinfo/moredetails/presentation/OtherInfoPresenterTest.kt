package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val repository: OtherInfoRepository = mockk()
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk()
    private val presenter: OtherInfoPresenter =
        OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)

    @Test
    fun `getArtistInfo should return artist biography ui state`() {
        val artistBiography = ArtistBiography("artistName", "biography", "articleUrl")
        every { repository.getArtistInfo("artistName") } returns artistBiography
        every { artistBiographyDescriptionHelper.getDescription(artistBiography) } returns "description"
        val artistBiographyTester: (CardUiState) -> Unit = mockk(relaxed = true)

        presenter.artistBiographyObservable.subscribe(artistBiographyTester)
        presenter.getArtistInfo("artistName")

        val result = CardUiState("artistName", "description", "articleUrl")
        verify { artistBiographyTester(result) }
    }
}