package ayds.songinfo.moredetails.presentation

data class CardUiState(
    val artistName:String,
    val contentHTML:String,
    val URL:String,
    val imageUrl: String =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
)
