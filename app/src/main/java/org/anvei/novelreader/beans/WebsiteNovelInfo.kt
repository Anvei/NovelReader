package org.anvei.novelreader.beans

import androidx.annotation.IntRange
import org.anvei.novelreader.novel.WebsiteIdentifier
import java.io.Serializable

class WebsiteNovelInfo(
    val identifier: WebsiteIdentifier,
    val novelName: String
) : Serializable {
    var author : String? = null
    var novelUrl : String? = null
    var coverUrl : String? = null
    var intro : String? = null
    private val chapterInfoList: MutableList<WebsiteChapterInfo> = ArrayList()

    fun add(chapterInfo: WebsiteChapterInfo) {
        chapterInfoList.add(chapterInfo)
    }

    fun getChapterInfo(@IntRange(from = 1) index: Int): WebsiteChapterInfo {
        return chapterInfoList[index - 1]
    }
}