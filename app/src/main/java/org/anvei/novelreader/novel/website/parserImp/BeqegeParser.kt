package org.anvei.novelreader.novel.website.parserImp

import org.anvei.novelreader.beans.WebsiteChapter
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.novel.website.WebsiteIdentifier
import org.anvei.novelreader.novel.website.WebsiteNovelParser
import org.jsoup.Jsoup
import java.io.IOException

class BeqegeParser : WebsiteNovelParser() {

    companion object {
        private const val homeUrl = "https://www.beqege.cc"
        private const val searchApi = "$homeUrl/search.php"
        private const val searchKey = "keyword"
        private const val SELECT_NOVEL_LIST = "#main > div > div.panel-body > ul > li"
        private const val SELECT_CHAPTER_LIST = "#list > dd > a"
        private const val SELECT_CHAPTER_CONTENT = "#content > p"
    }

    override fun getWebsiteIdentifier(): WebsiteIdentifier {
        return WebsiteIdentifier.UNKNOWN
    }

    /**
     * 保证得到的WebsiteNovel的website、author、novelName、novelUrl不为null
     */
    override fun search(keyWord: String): List<WebsiteNovel> {
        val novelInfoList: MutableList<WebsiteNovel> = ArrayList()
        try {
            val document = Jsoup.connect(searchApi)
                .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                .timeout(timeOut)
                .data(searchKey, keyWord)
                .post()
            val divs = document.select(SELECT_NOVEL_LIST)
            for (div in divs) {
                val novel = WebsiteNovel().apply {
                    website = websiteIdentifier
                    author = div.select("span.s4").text()
                    novelName = div.select("span.s2 > a").text()
                    novelUrl = div.select("span.s2 > a").attr("href")
                }
                novelInfoList.add(novel)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (filter == null) novelInfoList else filter.filter(novelInfoList)
    }

    override fun searchByAuthor(author: String): List<WebsiteNovel> {
        return search(author)
    }

    override fun searchByNovelName(name: String): List<WebsiteNovel> {
        return search(name)
    }

    /**
     * novelInfo的novelUrl不能为null
     */
    override fun loadNovel(novelInfo: WebsiteNovel): List<WebsiteChapter> {
        return loadNovel(novelInfo.novelUrl!!)
    }

    /**
     * 保证加载得到的WebsiteChapter的index、chapterName、chapterUrl已知
     */
    override fun loadNovel(novelUrl: String): List<WebsiteChapter> {
        val chapterInfoList: MutableList<WebsiteChapter> = ArrayList()
        try {
            val document = getDocument(novelUrl)
            val elements = document.select(SELECT_CHAPTER_LIST)
            var count = 1
            for (element in elements) {
                val chapter = WebsiteChapter().apply {
                    index = count
                    chapterName = element.text()
                    chapterUrl = homeUrl + element.attr("href")
                }
                chapterInfoList.add(chapter)
                count++
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return chapterInfoList
    }

    /**
     * 保证章节内容chapterContent的加载
     */
    override fun loadChapter(websiteChapter: WebsiteChapter): WebsiteChapter {
        val content = StringBuilder()
        try {
            val document = getDocument(websiteChapter.chapterUrl!!)
            val paras = document.select(SELECT_CHAPTER_CONTENT)
            for (i in 1 until paras.size) {
                content.append(PARA_PREFIX)
                    .append(paras[i].text().trim { it <= ' ' })
                    .append(PARA_SUFFIX)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        websiteChapter.chapterContent = content.toString()
        return websiteChapter
    }
}