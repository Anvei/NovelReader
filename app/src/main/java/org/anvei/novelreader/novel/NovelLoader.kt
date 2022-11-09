package org.anvei.novelreader.novel

import org.anvei.novelreader.beans.Novel

interface NovelLoader {
    fun load(): Novel
}