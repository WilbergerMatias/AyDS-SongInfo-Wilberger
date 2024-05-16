package ayds.songinfo.moredetails.presentation


import ayds.observer.Observer
import ayds.observer.Subject
import ayds.songinfo.home.view.HomeUiState
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterImplTest{
    private val artistName:String = "artistName"
    private val artistBiography:ArtistBiography = mockk(relaxUnitFun = true)
    private val repository: OtherInfoRepository = mockk{every {getArtistInfo(artistName)} returns artistBiography }
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk(relaxUnitFun = true)

    private val artistBiographyUiState:ArtistBiographyUiState = mockk{ every { infoHTML } returns artistBiographyDescriptionHelper.getDescription(artistBiography) }
    private val onActionSubject = Subject<ArtistBiographyUiState>()
    private val presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)

    @Test
    fun `should notify observer with correct ui state when artist info is retrieved`() {
        // Given
        presenter.getArtistInfo(artistBiography.artistName)
        verify{onActionSubject.notify(artistBiographyUiState)}
    }

    //si el test me tira bronca, provar modificar el mockk del description helper para que devuelva el string como en el presenter
}