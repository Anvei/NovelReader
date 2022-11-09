package org.anvei.novelreader.novel.parser

import org.anvei.novelreader.beans.Chapter
import org.anvei.novelreader.beans.WebsiteChapterInfo
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.novel.WebsiteIdentifier
import org.anvei.novelreader.novel.WebsiteNovelParser
import org.jsoup.Jsoup
import java.io.IOException

class BeqegeParser : WebsiteNovelParser() {
    override fun getWebsiteIdentifier(): WebsiteIdentifier {
        return WebsiteIdentifier.UNKNOWN
    }

    override fun search(keyWord: String): List<WebsiteNovelInfo> {
        val novelInfoList: MutableList<WebsiteNovelInfo> = ArrayList()
        try {
            val document = Jsoup.connect(searchApi)
                .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                .timeout(timeOut)
                .data(searchKey, keyWord)
                .post()
            val divs = document.select(SELECT_NOVEL_LIST)
            for (div in divs) {
                val novelUrl = div.select("span.s2 > a").attr("href")
                val novelName = div.select("span.s2 > a").text()
                val author = div.select("span.s4").text()
                val novelInfo = WebsiteNovelInfo(websiteIdentifier, novelName, WebsiteNovelInfo.Status.UNKNOWN)
                novelInfo.apply {
                    this.author = author
                    this.novelUrl = novelUrl
                }
                novelInfoList.add(novelInfo)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (filter == null) novelInfoList else filter.filter(novelInfoList)
    }

    override fun searchByAuthor(author: String): List<WebsiteNovelInfo> {
        return search(author)
    }

    override fun searchByNovelName(name: String): List<WebsiteNovelInfo> {
        return search(name)
    }

    override fun loadNovel(novelInfo: WebsiteNovelInfo): List<WebsiteChapterInfo> {
        return loadNovel(novelInfo.novelUrl!!)
    }

    override fun loadNovel(novelUrl: String): List<WebsiteChapterInfo> {
        val chapterInfoList: MutableList<WebsiteChapterInfo> = ArrayList()
        try {
            val document = getDocument(novelUrl)
            val elements = document.select(SELECT_CHAPTER_LIST)
            for (element in elements) {
                val chapterUrl = homeUrl + element.attr("href")
                val chapterName = element.text()
                val chapterInfo = WebsiteChapterInfo(chapterName)
                chapterInfo.chapterUrl = chapterUrl
                chapterInfoList.add(chapterInfo)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return chapterInfoList
    }

    override fun loadChapter(chapterInfo: WebsiteChapterInfo): Chapter {
        val content = StringBuilder()
        try {
            val document = getDocument(chapterInfo.chapterUrl!!)
            val paras = document.select(SELECT_CHAPTER_CONTENT)
            for (i in 1 until paras.size) {
                content.append(PARA_PREFIX)
                    .append(paras[i].text().trim { it <= ' ' })
                    .append(PARA_SUFFIX)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Chapter(chapterInfo.chapterName, content.toString())
    }

    companion object {
        private const val homeUrl = "https://www.beqege.cc"
        private const val searchApi = "$homeUrl/search.php"
        private const val searchKey = "keyword"
        private const val SELECT_NOVEL_LIST = "#main > div > div.panel-body > ul > li"
        private const val SELECT_CHAPTER_LIST = "#list > dd > a"
        private const val SELECT_CHAPTER_CONTENT = "#content > p"
    }
}