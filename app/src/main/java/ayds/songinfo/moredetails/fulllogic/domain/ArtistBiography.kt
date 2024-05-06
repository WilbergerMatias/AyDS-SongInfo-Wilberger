package ayds.songinfo.moredetails.fulllogic.domain

data class ArtistBiography
    (val artistName: String, val biography: String, val articleUrl: String)

fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")
