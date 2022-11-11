package org.anvei.novelreader.novel.parser

import org.anvei.novelreader.beans.Chapter
import org.anvei.novelreader.beans.WebsiteChapterInfo
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.novel.WebsiteIdentifier
import org.anvei.novelreader.novel.WebsiteNovelParser
import org.jsoup.Jsoup
import java.io.IOException

class W147xsParser : WebsiteNovelParser() {
    override fun getWebsiteIdentifier(): WebsiteIdentifier {
        return WebsiteIdentifier.W147XS
    }

    override fun search(keyWord: String): List<WebsiteNovelInfo> {
        val novelInfoList: MutableList<WebsiteNovelInfo> = ArrayList()
        try {
            val document = Jsoup.connect(searchApi)
                .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                .timeout(timeOut)
                .data("keyword", keyWord)
                .post()
            val elements = document.select(SELECT_NOVEL_LIST)
            for (element in elements) {
                val novelUrl = element.select("td:nth-child(2) > a").attr("href")
                val novelName = element.select("td:nth-child(2) > a").text()
                val author = element.select("td:nth-child(4)").text()
                val novelInfo = WebsiteNovelInfo(websiteIdentifier, novelName)
                novelInfo.apply {
                    this.novelUrl = novelUrl
                    this.author = author
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
                val chapterName = element.text()
                val chapterUrl = homeUrl + element.attr("href")
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
        val stringBuilder = StringBuilder()
        try {
            val document = getDocument(chapterInfo.chapterUrl!!)
            val paras = document.select(SELECT_CHAPTER_CONTENT) // 获取<p></p>标签
            for (para in paras) {
                val paraText = para.text().trim { it <= ' ' }
                if (!paraText.startsWith("【")) {      // 过滤网站的垃圾信息
                    stringBuilder.append(PARA_PREFIX)
                        .append(para.text().trim { it <= ' ' })
                        .append(PARA_SUFFIX)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Chapter(chapterInfo.chapterName, stringBuilder.toString())
    }

    companion object {
        private const val homeUrl = "https://www.147xs.org"
        private const val searchApi = "https://www.147xs.org/search.php"
        private const val SELECT_NOVEL_LIST = "#bookcase_list > tr"
        private const val SELECT_CHAPTER_LIST = "#list > dl > dd > a"
        private const val SELECT_CHAPTER_CONTENT = "#content > p"
    }
}