package org.anvei.novelreader.interfaces

interface IBookShelfView {

    /**
     * 用来从当前书架上删除指定下标的小说
     */
    fun deleteNovel(index: Int)
}