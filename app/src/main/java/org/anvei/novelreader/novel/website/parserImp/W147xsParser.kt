package org.anvei.novelreader.novel.website.parserImp

import org.anvei.novelreader.beans.WebsiteChapter
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.novel.website.WebsiteIdentifier
import org.anvei.novelreader.novel.website.WebsiteNovelParser
import org.jsoup.Jsoup
import java.io.IOException

class W147xsParser : WebsiteNovelParser() {

    companion object {
        private const val homeUrl = "https://www.147xs.org"
        private const val searchApi = "https://www.147xs.org/search.php"
        private const val SELECT_NOVEL_LIST = "#bookcase_list > tr"
        private const val SELECT_CHAPTER_LIST = "#list > dl > dd > a"
        private const val SELECT_CHAPTER_CONTENT = "#content > p"
    }

    override fun getWebsiteIdentifier(): WebsiteIdentifier {
        return WebsiteIdentifier.W147XS
    }

    // 保证website、author、novelName、novelUrl的加载
    override fun search(keyWord: String): List<WebsiteNovel> {
        val novelList: MutableList<WebsiteNovel> = ArrayList()
        try {
            val document = Jsoup.connect(searchApi)
                .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                .timeout(timeOut)
                .data("keyword", keyWord)
                .post()
            val elements = document.select(SELECT_NOVEL_LIST)
            for (element in elements) {
                val novel = WebsiteNovel().apply {
                    website = websiteIdentifier
                    author = element.select("td:nth-child(4)").text()
                    novelName = element.select("td:nth-child(2) > a").text()
                    novelUrl = element.select("td:nth-child(2) > a").attr("href")
                }
                novelList.add(novel)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (filter == null) novelList else filter.filter(novelList)
    }

    override fun searchByAuthor(author: String): List<WebsiteNovel> {
        return search(author)
    }

    override fun searchByNovelName(name: String): List<WebsiteNovel> {
        return search(name)
    }

    override fun loadNovel(novelInfo: WebsiteNovel): List<WebsiteChapter> {
        return loadNovel(novelInfo.novelUrl!!)
    }

    override fun loadNovel(novelUrl: String): List<WebsiteChapter> {
        val chapterList: MutableList<WebsiteChapter> = ArrayList()
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
                chapterList.add(chapter)
                count++
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return chapterList
    }

    override fun loadChapter(websiteChapter: WebsiteChapter): WebsiteChapter {
        val stringBuilder = StringBuilder()
        try {
            val document = getDocument(websiteChapter.chapterUrl!!)
            val paras = document.select(SELECT_CHAPTER_CONTENT) // 获取<p></p>标签
            for (para in paras) {
                val paraText = para.text().trim { it <= ' ' }
                // 过滤网站的垃圾信息、可能会过滤掉小说正文内容
                if (!paraText.startsWith("【")) {
                    stringBuilder.append(PARA_PREFIX)
                        .append(para.text().trim { it <= ' ' })
                        .append(PARA_SUFFIX)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        websiteChapter.chapterContent = stringBuilder.toString()
        return websiteChapter
    }
}