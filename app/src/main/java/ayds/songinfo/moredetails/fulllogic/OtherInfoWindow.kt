package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val AUDIOSCROBBLER_URL = "https://ws.audioscrobbler.com/2.0/"

private const val LASTFM_LOGO_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

private const val ARTICLE_DB_NAME = "database-article"

data class ArtistBiography(val artistName: String, val biography: String, val articleUrl: String)

class OtherInfoWindow : Activity() {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private lateinit var articleTextView: TextView
    private lateinit var articleDB: ArticleDatabase
    private lateinit var lastFMAPIService: LastFMAPI
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

    //VIEW
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        prepArticleDatabase()
        prepViewProperties()
        createLastFMAPI()
        getArtistInfoAsync()
    }

    private fun createLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIOSCROBBLER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPIService = retrofit.create(LastFMAPI::class.java)
    }

    private fun prepViewProperties(){
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

    private fun prepArticleDatabase() {
        articleDB =
            databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_DB_NAME).build()
    }

    private fun getArtistInfoAsync(){
        Thread{
            getArtistInfo()
        }.start()
    }

    //DB search and UI update
    private fun getArtistInfo() {
        val artistBiography = getArtistInfoFromRepository()
        updateUI(artistBiography)
    }

    //DB or API search, DB update,
    private fun getArtistInfoFromRepository(): ArtistBiography{
        val artistName = getArtistName()
        val dbArticle = getArticleFromDB(artistName)
        val artistBiography: ArtistBiography

        if (dbArticle == null){
            artistBiography = getArticleFromService(artistName)
            if(artistBiography.biography.isNotEmpty()){
                insertArtistIntoDB(artistBiography)
            }
        }else {
            artistBiography = dbArticle.markItAsLocal()
        }
        return artistBiography
    }

    // DB internal notation
    private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")

    //get article from DB
    private fun getArticleFromDB(artistName: String): ArtistBiography? {
        val artistEntity = articleDB.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }

    //API service call
    private fun getArticleFromService(artistName: String): ArtistBiography {
        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return artistBiography
    }

    //API service con+information parse
    private fun getArtistBioFromExternalData(serviceData: String?, artistName: String): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }


    private fun getSongFromService(artistName: String) =
        lastFMAPIService.getArtistInfo(artistName).execute()

    //DB update
    private fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        articleDB.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }

    //UI update
    private fun updateUI(artistBiography: ArtistBiography) {
        runOnUiThread {
            updateOpenUrlButton(artistBiography)
            updateLastFMLogo()
            updateArticleText(artistBiography)
        }
    }

    //URLbutton behaviour link update
    private fun updateOpenUrlButton(artistBiography: ArtistBiography) {
        openUrlButton.setOnClickListener {
            navigateToUrl(artistBiography.articleUrl)
        }
    }

    //button action
    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    //img view update
    private fun updateLastFMLogo() {
        Picasso.get().load(LASTFM_LOGO_URL).into(lastFMImageView)
    }

    //
    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    //text view update
    private fun updateArticleText(artistBiography: ArtistBiography) {
        val text = artistBiography.biography.replace("\\n", "\n")
        articleTextView.text = Html.fromHtml(textToHtml(text, artistBiography.artistName))
    }

    //text formatter
    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}
