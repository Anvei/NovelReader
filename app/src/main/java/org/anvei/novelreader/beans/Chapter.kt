package org.anvei.novelreader.beans

import java.io.Serializable

// 表示一个章节
class Chapter : Serializable {
    val chapterName: String
    var chapterContent: String? = null
    var chapterIndex: Int = 0

    constructor(chapterName: String) {
        this.chapterName = chapterName
    }

    constructor(chapterName: String, chapterContent: String?) {
        this.chapterName = chapterName
        this.chapterContent = chapterContent
    }
}