package org.anvei.novelreader.novel.website.parserImp

import org.anvei.novelreader.beans.WebsiteChapter
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.novel.website.WebsiteIdentifier
import org.anvei.novelreader.novel.website.WebsiteNovelParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class BiqumuParser : WebsiteNovelParser() {

    companion object {
        private const val homeUrl = "http://www.biqumu.com"
        private const val searchApi = "http://www.biqumu.com/search.html"
        private const val searchKey = "s"
        private const val SELECT_NOVEL_LIST =
            "body > div.container > div:nth-child(1) > div > ul > li"
        private const val SELECT_CHAPTER_LIST = "body > div.container > div:nth-child(2) > div > ul"
        private const val SELECT_CHAPTER_CONTENT =
            "body > div.container > div.row.row-detail > div > div > p"
        private const val SELECT_NEXT_CHAPTER =
            "body > div.container > div.row.row-detail div.read_btn > a:nth-child(4)"
        private const val SELECT_OPTION =
            "body > div.container > div:nth-child(2) > div > div:nth-child(4) > select > option"
    }

    override fun getWebsiteIdentifier(): WebsiteIdentifier {
        return WebsiteIdentifier.BIQUMU
    }

    @Throws(IOException::class)
    private fun getSearchResult(keyWord: String): Document {
        return Jsoup.connect(searchApi)
            .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
            .data(searchKey, keyWord)
            .timeout(timeOut)
            .post()
    }

    /**
     * 保证加载出小说名称、作者、链接
     */
    override fun search(keyWord: String): List<WebsiteNovel> {
        val novelInfoList: MutableList<WebsiteNovel> = ArrayList()
        try {
            val document = getSearchResult(keyWord)
            val lis = document.select(SELECT_NOVEL_LIST)
            for (li in lis) {
                val novel = WebsiteNovel().apply {
                    website = websiteIdentifier
                    author = li.select("span.n4").text()
                    novelName = li.select("span.n2 > a").text()
                    novelUrl = homeUrl + li.select("span.n2 > a").attr("href")
                }
                novelInfoList.add(novel)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return filter?.filter(novelInfoList) ?: novelInfoList
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
        val chapterInfoList: MutableList<WebsiteChapter> = ArrayList()
        try {
            // 多页目录加载
            // 先加载首页
            val document = getDocument(novelUrl)
            var lis = document.select("$SELECT_CHAPTER_LIST:nth-child(4) > li")
            var count = 1
            for (li in lis) {
                val chapter = WebsiteChapter().apply {
                    index = count
                    chapterName = li.select("a").text()
                    chapterUrl = homeUrl + li.select("a").attr("href")
                }
                chapterInfoList.add(chapter)
                count++
            }
            val others: MutableList<Document> = ArrayList()
            val otherPage = getDocument(novelUrl + "1/")
            val options = otherPage.select(SELECT_OPTION) // 处理多页目录
            if (options.size != 0) {
                others.add(otherPage)
                for (i in 2 until options.size) {
                    others.add(getDocument(homeUrl + options[i].attr("value")))
                }
            }
            for (doc in others) {
                // 处理网站的反爬虫设计障碍
                val style = doc.select("head style").toString()
                var start = 0
                var last = 0
                val splits = style.split("[()]").toTypedArray()
                run {
                    var i = 1
                    while (i < splits.size) {
                        val temp = splits[i].toInt()
                        last = if (temp > last) {
                            start++
                            temp
                        } else {
                            break
                        }
                        i += 2
                    }
                }
                val end = (splits.size - 1) / 2 - start
                lis = doc.select("$SELECT_CHAPTER_LIST > li")
                for (i in start until lis.size - end) {
                    val chapter = WebsiteChapter().apply {
                        index = count
                        chapterName = lis[i].select("a").text()
                        chapterUrl = homeUrl + lis[i].select("a").attr("href")
                    }
                    chapterInfoList.add(chapter)
                    count++
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return chapterInfoList
    }

    override fun loadChapter(chapterInfo: WebsiteChapter): WebsiteChapter {
        val content = StringBuilder()
        try {
            var nextUrl: String = chapterInfo.chapterUrl!!
            var hasNext = true // 是否有下一页
            var isFirst = true
            while (hasNext) {
                val document = getDocument(nextUrl)
                nextUrl = homeUrl + document.select(SELECT_NEXT_CHAPTER).attr("href")
                hasNext = nextUrl[nextUrl.length - 7] == '_'
                val paras = document.select(SELECT_CHAPTER_CONTENT)
                // 去掉第二页之后的重复行
                var i: Int
                if (isFirst) {
                    i = 0
                    isFirst = false
                } else {
                    i = 1
                }
                while (i < paras.size) {
                    content.append(PARA_PREFIX)
                        .append(paras[i].text().trim { it <= ' ' })
                        .append(PARA_SUFFIX)
                    i++
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        chapterInfo.chapterContent = content.toString()
        return chapterInfo
    }
}