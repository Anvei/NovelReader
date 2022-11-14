package org.anvei.novelreader.novel.website.parserImp

import org.anvei.novelreader.exceptions.WebsiteLoadException
import org.anvei.novelreader.beans.WebsiteChapter
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.novel.website.WebsiteIdentifier
import org.anvei.novelreader.novel.website.WebsiteNovelParser
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
     * 保证website、author、novelName、novelUrl、coverUrl、Intro的加载
     * 可能会因为解析错误抛出异常
     */
    override fun search(keyWord: String): List<WebsiteNovel> {
        val novels: MutableList<WebsiteNovel> = ArrayList()
        val searchUrl = searchApiPrefix + transferHexString(keyWord) + searchApiSuffix
        try {
            val document = getDocument(searchUrl)
            val uls = document.select(SELECT_NOVEL_LIST)
            for (ul in uls) {
                val lis = ul.getElementsByTag("li")
                if (lis.size == 2) {
                    val novel = WebsiteNovel().apply {
                        website = websiteIdentifier
                        novelName = lis[0].select("img").attr("alt")
                        novelUrl = lis[1].select("strong > a").attr("href")
                        coverUrl = "https:" + lis[0].select("img").attr("src")
                        val tempTexts = lis[1].text().split(" ", limit = 5)
                        if (tempTexts.size != 5)
                            throw WebsiteLoadException(
                                "SfacgParser: search加载错误!"
                            )
                        author = tempTexts[2].substring(0, tempTexts[2].indexOf('/'))
                        intro = tempTexts[4]
                    }
                    novels.add(novel)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return filter?.filter(novels) ?: novels
    }

    override fun searchByAuthor(author: String): List<WebsiteNovel> {
        return search(author)
    }

    override fun searchByNovelName(novelName: String): List<WebsiteNovel> {
        return search(novelName)
    }

    override fun loadNovel(novelInfo: WebsiteNovel): List<WebsiteChapter> {
        return loadNovel(novelInfo.novelUrl!!)
    }

    override fun loadNovel(novelUrl: String): List<WebsiteChapter> {
        val url = "$novelUrl/MainIndex/"
        val chapterList: MutableList<WebsiteChapter> = ArrayList()
        try {
            val document = getDocument(url)
            val elements = document.select(SELECT_CHAPTER_LIST)
            var count = 1
            for (element in elements) {
                val chapter = WebsiteChapter().apply {
                    index = count
                    chapterName = element.attr("title")
                    chapterUrl = homeUrl.substring(0, homeUrl.length - 1) + element.attr("href")
                }
                chapterList.add(chapter)
                count++
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return chapterList
    }

    override fun loadChapter(websiteChapter: WebsiteChapter): WebsiteChapter {
        val content = StringBuilder()
        try {
            val document = getDocument(websiteChapter.chapterUrl!!)
            val paras = document.select(SELECT_CHAPTER_CONTENT)
            for (para in paras) {
                content.append(PARA_PREFIX)
                    .append(para.text().trim())
                    .append(PARA_SUFFIX)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        websiteChapter.chapterContent = content.toString()
        return websiteChapter
    }
}