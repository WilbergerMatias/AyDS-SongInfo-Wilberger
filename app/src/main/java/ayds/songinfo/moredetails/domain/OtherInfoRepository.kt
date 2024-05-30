package ayds.songinfo.moredetails.domain

import android.content.Context

interface OtherInfoRepository {

    fun getCard(artistName:String): Card
}
