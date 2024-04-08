package ayds.songinfo.home.view

import ayds.songinfo.home.controller.HomeControllerInjector
import ayds.songinfo.home.model.HomeModelInjector
import ayds.songinfo.home.view.songDescription.SongDescriptionHelper
import ayds.songinfo.home.view.songDescription.SongDescriptionHelperImpl
import ayds.songinfo.home.view.songDescription.ReleaseDateHelperFactoryImpl

object HomeViewInjector {

    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(ReleaseDateHelperFactoryImpl())

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}