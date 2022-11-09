package org.anvei.novelreader.activity.interfaces

interface IReadPage {
    fun onNextChapter()
    fun onLastChapter()
    fun onSettingView()
    fun onNovelHome()
    fun onChapterListView(flag: Boolean)
    fun onCurrentChapter(currentIndex: Int)
}