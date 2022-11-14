package org.anvei.novelreader.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.AppConfig
import org.anvei.novelreader.adapter.ChapterContentAdapter
import org.anvei.novelreader.adapter.ChapterListAdapter
import org.anvei.novelreader.beans.WebsiteChapter
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.databinding.ActivityReadPageBinding
import org.anvei.novelreader.interfaces.IReadPageView
import org.anvei.novelreader.novel.website.NovelParserFactory
import org.anvei.novelreader.novel.website.WebsiteNovelParser
import org.anvei.novelreader.room.repository.NovelRepository
import org.anvei.novelreader.viewmodel.ReadPageActivityModel
import java.util.*
import kotlin.collections.ArrayList

class ReadPageActivity : BaseActivity(), IReadPageView {

    companion object {
        /* 用来启动ReadPageActivity */
        fun startActivity(context: Context, novelInfo: WebsiteNovel) {
            val intent = Intent(context, ReadPageActivity::class.java)
            intent.putExtra(EXTRA_NOVEL_INFO, novelInfo)
            context.startActivity(intent)
        }
    }

    private lateinit var viewBinding: ActivityReadPageBinding
    private lateinit var viewModel: ReadPageActivityModel

    // 小说基本信息
    private lateinit var novelParser: WebsiteNovelParser
    private lateinit var currentNovel: WebsiteNovel

    private val chapterInfoList = ArrayList<WebsiteChapter>()

    private lateinit var chapterListAdapter: ChapterListAdapter
    private lateinit var chapterContentAdapter: ChapterContentAdapter

    private var currentChapterIndex: Int = 0

    /**
     * 初始化小说基本信息、小说解析器
     */
    private fun initNovelConfig() {
        currentNovel = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as WebsiteNovel
        // 根据小说网站标识符初始化相应的网络小说解析器
        novelParser = NovelParserFactory.getParser(currentNovel.website)
    }

    private fun initComponent() {
        // 初始化小说内容ChapterContentRecycler
        chapterContentAdapter = ChapterContentAdapter(chapterInfoList, this, novelParser)
        viewBinding.chapterContentRecycler.adapter = chapterContentAdapter
        viewBinding.chapterContentRecycler.layoutManager = LinearLayoutManager(this)
        // 初始化小说章节列表ChapterListRecycler
        chapterListAdapter = ChapterListAdapter(chapterInfoList, this)
        viewBinding.chapterListRecycler.adapter = chapterListAdapter
        viewBinding.chapterListRecycler.layoutManager = LinearLayoutManager(this)

        viewBinding.chapterContentRecycler.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager = recyclerView.layoutManager as LinearLayoutManager
                val index = manager.findFirstVisibleItemPosition()
                if (index != currentChapterIndex) {
                    val temp = currentChapterIndex
                    currentChapterIndex = index
                    chapterListAdapter.notifyItemChanged(index)
                    chapterListAdapter.notifyItemChanged(temp)
                    onCurrentChapterName()
                }
            }
        })

        viewBinding.readPageNovelTitle.text = currentNovel.novelName
        initSettingView()
        initHeaderAndFooterView()
    }

    /**
     * 初始化设置视图
     */
    private fun initSettingView() {
        viewBinding.lastChapter.setOnClickListener {
            onLastChapter()
        }
        viewBinding.nextChapter.setOnClickListener {
            onNextChapter()
        }
        viewBinding.rpSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onSeekBarProgressChange(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
        viewBinding.chapterListDisplayBtn.setOnClickListener {
            onChapterListView(true)
        }
        viewBinding.chapterPageNovelHomeBtn.setOnClickListener {
            onNovelHome()
        }
        viewBinding.chapterFontSettingBtn.setOnClickListener {
            onSettingView()         // 关闭设置面板的主视图
            viewBinding.rpSettingFontLinear.visibility = View.VISIBLE
        }
        viewBinding.rpFontSizeAdd.setOnClickListener {
            if (chapterContentAdapter.getContentSize() < 24) {
                chapterContentAdapter.apply {
                    setContentSize(getContentSize() + 2)
                }
                AppConfig.setNovelContentFontSize(chapterContentAdapter.getContentSize())
                toast("当前字体大小: ${chapterContentAdapter.getContentSize().toInt()}sp")
            } else {
                toast("已经是最大值")
            }
        }
        viewBinding.rpFontSizeSubtract.setOnClickListener {
            if (chapterContentAdapter.getContentSize() > 10) {
                chapterContentAdapter.apply {
                    setContentSize(getContentSize() - 2)
                }
                AppConfig.setNovelContentFontSize(chapterContentAdapter.getContentSize())
                toast("当前字体大小: ${chapterContentAdapter.getContentSize().toInt()}sp")
            } else {
                toast("已经是最小值")
            }
        }
    }

    /**
     * 初始化页眉、页脚视图，包括当前章节名称显示、当前时间、当前小说名
     */
    @SuppressLint("SetTextI18n")
    private fun initHeaderAndFooterView() {
        onCurrentChapterName()
        viewBinding.rpBottomViewTime.format24Hour = "hh:mm"
        viewBinding.rpBottomViewNovelName.text = currentNovel.novelName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityReadPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModel = ViewModelProvider(this).get(ReadPageActivityModel::class.java)

        viewModel.liveData.observe(this) {
        }
        initNovelConfig()
        initComponent()
        onProgressBar(true)
        Thread {
            // 加载小说章节信息列表
            chapterInfoList.addAll(novelParser.loadNovel(currentNovel.novelUrl))
            runOnUiThread {
                onProgressBar(false)
                onCurrentChapterName()
                if (chapterInfoList.size == 0) {
                    toast("该小说为空")
                    finish()
                }
                chapterListAdapter.notifyItemRangeChanged(0, chapterInfoList.size)
                chapterContentAdapter.notifyItemRangeChanged(0, chapterInfoList.size)
            }
        }.start()
    }

    // 更新最后阅读时间
    override fun onStop() {
        super.onStop()
        updateReadInfo()
    }

    override fun onNextChapter() {
        if (currentChapterIndex < chapterInfoList.size) {
            currentChapterIndex++
            viewBinding.chapterContentRecycler.scrollToPosition(currentChapterIndex)
        } else {
            Toast.makeText(this, "已经是最后一章!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLastChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--
            viewBinding.chapterContentRecycler.scrollToPosition(currentChapterIndex)
        } else {
            Toast.makeText(this, "已经是第一章!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSettingView() {
        // 主设置视图可见时，字体设置视图必不可见
        // 主设置视图不可见时，如果字体设置视图可见就关闭，否则就打开主设置视图
        if (viewBinding.chapterSetting.visibility == View.GONE) {
            if (viewBinding.rpSettingFontLinear.visibility == View.VISIBLE) {
                viewBinding.rpSettingFontLinear.visibility = View.GONE
            } else {
                viewBinding.chapterSetting.visibility = View.VISIBLE
            }
        } else {
            viewBinding.chapterSetting.visibility = View.GONE
        }
    }

    override fun onNovelHome() {
        NovelHomeActivity.startActivity(this, currentNovel)
    }

    override fun onChapterListView(flag: Boolean) {
        // 打开章节列表侧栏的同时需要让章节列表视图跳转到当前显示的章节
        viewBinding.chapterListRecycler.scrollToPosition(currentChapterIndex)
        viewBinding.readPageDrawer.openDrawer(GravityCompat.START)
        onSettingView()         // 关闭章节设置面板
    }

    override fun onCurrentChapter(index: Int) {
        viewBinding.chapterContentRecycler.scrollToPosition(index)
        // viewBinding.chapterContentRecycler.smoothScrollToPosition(index)
        setSeekBarProgress(index * 100 / chapterInfoList.size)
    }

    fun setCurrentIndex(index: Int) {
        currentChapterIndex = index
    }

    fun onCurrentChapterName() {
        if (currentChapterIndex < chapterInfoList.size) {
            viewBinding.rpHeaderChapterName.text = chapterInfoList[currentChapterIndex].chapterName
        }
    }

    override fun getCurrentIndex(): Int {
        return currentChapterIndex
    }

    override fun onProgressBar(flag: Boolean) {
        viewBinding.chapterContentProgress.visibility = if (flag) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // 退出时更新相关阅读信息
    override fun updateReadInfo() {
        Thread {
            NovelRepository.updateBookShelfNovel(
                currentNovel.apply {
                    lastReadChapterIndex = currentChapterIndex
                    lastReadTime = java.sql.Date(Date().time)
                    lastChapterName = chapterInfoList.last().chapterName
                }
            )
        }.start()
    }

    fun onSeekBarProgressChange(progress: Int) {
        val targetIndex = chapterInfoList.size * progress / 100
        onCurrentChapter(targetIndex)
    }

    override fun setSeekBarProgress(progress: Int) {
        viewBinding.rpSeekbar.progress = progress
    }

}