package org.anvei.novelreader.novel.parser

import org.anvei.novelreader.beans.Chapter
import org.anvei.novelreader.beans.WebsiteChapterInfo
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.novel.WebsiteIdentifier
import org.anvei.novelreader.novel.WebsiteNovelParser
import java.io.IOException
import java.util.*

class SfacgParser : WebsiteNovelParser() {

    companion object {
        // The home page address of the SFACG website
        private const val homeUrl = "https://book.sfacg.com/"

        //  prefix of the query API
        private const val searchApiPrefix = "http://s.sfacg.com/?Key="

        // suffix of the query API
        private const val searchApiSuffix = "&S=1&SS=0"

        // css selector string for getting novel list
        private const val SELECT_NOVEL_LIST =
            "#form1 > table.comic_cover.Height_px22.font_gray.space10px > tbody > tr > td > ul"

        // css selector string for getting chapter list
        private const val SELECT_CHAPTER_LIST =
            "body > div.container > div.wrap.s-list > div.story-catalog > div.catalog-list > ul > li > a"

        //  css selector string for getting chapter content
        private const val SELECT_CHAPTER_CONTENT = "#ChapterBody > p"
    }

    override fun getWebsiteIdentifier(): WebsiteIdentifier {
        return WebsiteIdentifier.SFACG
    }

    /**
     * Escape the keyword string into the hexadecimal string format required for the query URL
     */
    private fun transferHexString(key: String): String {
        val stringBuilder = StringBuilder()
        for (element in key) {
            //  Java does not allow dangerous characters in URLs, so '%' needs to be expressed as %25
            stringBuilder.append("%25u")
                .append(Integer.toHexString(element.code).uppercase())
        }
        return stringBuilder.toString()
    }

    /**
     * Android system requires that network requests need to be executed in child threads.
     */
    override fun search(keyWord: String): List<WebsiteNovelInfo> {
        val novels: MutableList<WebsiteNovelInfo> = ArrayList()
        val searchUrl = searchApiPrefix + transferHexString(keyWord) + searchApiSuffix
        try {
            val document = getDocument(searchUrl)
            val uls = document.select(SELECT_NOVEL_LIST)
            for (ul in uls) {
                val lis = ul.getElementsByTag("li")
                if (lis.size == 2) {
                    val coverUrl = "https:" + lis[0].select("img").attr("src")
                    val novelName = lis[0].select("img").attr("alt")
                    val novelUrl = lis[1].select("strong > a").attr("href")
                    val tempTexts = lis[1].text().split(" ", limit = 5)

                    val novelInfo = WebsiteNovelInfo(websiteIdentifier, novelName)

                    novelInfo.apply {
                        this.novelUrl = novelUrl
                        this.coverUrl = coverUrl
                        if (tempTexts.size == 5) {
                            author = tempTexts[2].substring(0, tempTexts[2].indexOf('/'))
                            intro = tempTexts[4]
                        }
                    }
                    novels.add(novelInfo)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return filter?.filter(novels) ?: novels
    }

    override fun searchByAuthor(author: String): List<WebsiteNovelInfo> {
        return search(author)
    }

    override fun searchByNovelName(novelName: String): List<WebsiteNovelInfo> {
        return search(novelName)
    }

    override fun loadNovel(novelInfo: WebsiteNovelInfo): List<WebsiteChapterInfo> {
        return loadNovel(novelInfo.novelUrl!!)
    }

    override fun loadNovel(novelUrl: String): List<WebsiteChapterInfo> {
        val url = "$novelUrl/MainIndex/"
        val chapterInfoList: MutableList<WebsiteChapterInfo> = ArrayList()
        try {
            val document = getDocument(url)
            val elements = document.select(SELECT_CHAPTER_LIST)
            for (element in elements) {
                val chapterUrl = homeUrl.substring(0, homeUrl.length - 1) + element.attr("href")
                val chapterName = element.attr("title")
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
            for (para in paras) {
                content.append(PARA_PREFIX)
                    .append(para.text().trim())
                    .append(PARA_SUFFIX)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Chapter(chapterInfo.chapterName, content.toString())
    }
}