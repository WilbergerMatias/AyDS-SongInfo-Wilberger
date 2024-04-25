package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

class OtherInfoWindow : Activity() {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private lateinit var textPane1: TextView
    private lateinit var dataBase: ArticleDatabase
    private lateinit var apiService: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane1 = findViewById(R.id.textPane1)
        dataBase = Room.databaseBuilder(
            this,
            ArticleDatabase::class.java, "database-name-thename"
        ).build()
        apiService = createLastFMAPI()
        open(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun open(artist: String?) {
        Thread {
            dataBase.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
        }.start()
        artist?.let { getArtistInfo(it) }
    }

    private fun getArtistInfo(artistName: String) {
        Thread {
            val article = dataBase.ArticleDao().getArticleByArtistName(artistName)
            val text = if (article != null) {
                "[*]" + article.biography
            } else {
                fetchArtistInfoFromAPI(artistName)
            }
            displayArtistInfo(text)
        }.start()
    }

    private fun fetchArtistInfoFromAPI(artistName: String): String {
        return try {
            val callResponse = apiService.getArtistInfo(artistName).execute()
            if (callResponse.isSuccessful) {
                parseArtistInfo(callResponse.body(), artistName)
            } else {
                "No Results"
            }
        } catch (e: IOException) {
            Log.e("TAG", "Error $e")
            e.printStackTrace()
            "Error fetching data"
        }
    }

    private fun parseArtistInfo(responseBody: String?, artistName: String): String {
        val gson = Gson()
        val jobj = gson.fromJson(responseBody, JsonObject::class.java)
        val artist = jobj.getAsJsonObject("artist")
        val bio = artist.getAsJsonObject("bio")
        val extract = bio.getAsJsonPrimitive("content")
        val url = artist.getAsJsonPrimitive("url")
        val text = extract?.asString?.replace("\\n", "\n") ?: ""
        saveToDatabase(artistName, text, url.asString)
        return textToHtml(text, artistName)
    }

    private fun saveToDatabase(artistName: String, biography: String, url: String) {
        Thread {
            dataBase.ArticleDao().insertArticle(ArticleEntity(artistName, biography, url))
        }.start()
    }

    private fun displayArtistInfo(text: String) {
        runOnUiThread {
            // Load image
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Picasso.get().load(imageUrl).into(findViewById<ImageView>(R.id.imageView1))

            // Display text
            textPane1.text = Html.fromHtml(
                text,
                Html.FROM_HTML_MODE_COMPACT
            )
        }
    }

    private fun createLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace("(?i)$term".toRegex(), "<b>${term.uppercase()}</b>")
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}
