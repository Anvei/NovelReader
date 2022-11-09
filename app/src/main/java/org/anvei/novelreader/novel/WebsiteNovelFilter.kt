package org.anvei.novelreader.novel

import org.anvei.novelreader.beans.WebsiteNovelInfo

interface WebsiteNovelFilter {
    fun filter(novelInfoList: List<WebsiteNovelInfo>): List<WebsiteNovelInfo>
}