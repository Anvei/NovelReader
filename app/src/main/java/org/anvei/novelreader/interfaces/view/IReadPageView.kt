package org.anvei.novelreader.interfaces.view

// 由小说阅读视图负责实现
interface IReadPageView {

    /**
     * 下一章
     */
    fun onNextChapter()

    /**
     * 上一章
     */
    fun onLastChapter()

    /**
     * 如果当前章节设置面板可见，就关闭面板；
     * 如果不可见，就打开面板
     */
    fun onSettingView()

    /**
     * 打开小说主页
     */
    fun onNovelHome()

    /**
     * 控制左侧章节列表视图的显示
     */
    fun onChapterListView(flag: Boolean)

    /**
     * 获取当前小说章节号
     */
    fun getCurrentIndex(): Int

    /**
     * 跳转到当前currentIndex的章节
     */
    fun onCurrentChapter(index: Int)

    /**
     * 控制ProgressBar的显示
     */
    fun onProgressBar(flag: Boolean)

    /**
     * 更新最后阅读时间
     */
    fun updateLastReadTime()
}