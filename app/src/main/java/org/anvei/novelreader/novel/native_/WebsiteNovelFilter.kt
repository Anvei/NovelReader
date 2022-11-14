package org.anvei.novelreader.novel.native_

import org.anvei.novelreader.beans.WebsiteNovel

interface WebsiteNovelFilter {
    fun filter(novelInfoList: List<WebsiteNovel>): List<WebsiteNovel>
}