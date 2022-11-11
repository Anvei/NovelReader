package org.anvei.novelreader.interfaces.view

interface IReadPageView {
    fun onNextChapter()
    fun onLastChapter()
    fun onSettingView()
    fun onNovelHome()
    fun onChapterListView(flag: Boolean)
    fun onCurrentIndex(currentIndex: Int)
    fun onCurrentChapter()
    fun onProgressBar(flag: Boolean)
}