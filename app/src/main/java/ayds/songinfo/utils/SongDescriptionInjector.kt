package ayds.songinfo.utils

import ayds.songinfo.home.model.SongDateHelper
import ayds.songinfo.home.model.SongDateHelperImpl


object SongDescriptionInjector {

    val songDateHelper: SongDateHelper = SongDateHelperImpl()

}