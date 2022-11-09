package org.anvei.novelreader.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.anvei.novelreader.activity.interfaces.IReadPage
import org.anvei.novelreader.adapter.ChapterContentAdapter
import org.anvei.novelreader.adapter.ChapterListAdapter
import org.anvei.novelreader.beans.WebsiteChapterInfo
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.databinding.ActivityReadPageBinding
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteNovelParser
import org.anvei.novelreader.viewmodel.ReadPageActivityModel

class ReadPageActivity : BaseActivity(), IReadPage {

    companion object {
        /* 用来启动ReadPageActivity */
        fun startActivity(context: Context, novelInfo: WebsiteNovelInfo) {
            val intent = Intent(context, ReadPageActivity::class.java)
            intent.putExtra(EXTRA_NOVEL_INFO, novelInfo)
            context.startActivity(intent)
        }
    }

    private lateinit var viewBinding: ActivityReadPageBinding
    private lateinit var viewModel: ReadPageActivityModel
    // 小说基本信息
    private lateinit var novelParser: WebsiteNovelParser
    private lateinit var novelInfo: WebsiteNovelInfo

    private val chapterInfoList = ArrayList<WebsiteChapterInfo>()

    private lateinit var chapterListAdapter: ChapterListAdapter
    private lateinit var chapterContentAdapter: ChapterContentAdapter

    private var currentChapterIndex: Int = 0

    /**
     * 初始化小说基本信息、章节信息、小说解析器
     */
    private fun initNovelConfig() {
        novelInfo = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as WebsiteNovelInfo
        // 根据小说网站标识符初始化相应的网络小说解析器
        novelParser = NovelParserFactory.getParser(novelInfo.identifier)
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

        viewBinding.readPageNovelTitle.text = novelInfo.novelName
        viewBinding.lastChapter.setOnClickListener {
            onLastChapter()
        }
        viewBinding.nextChapter.setOnClickListener {
            onNextChapter()
        }
        viewBinding.chapterListDisplayBtn.setOnClickListener {
            onChapterListView(true)
        }
        viewBinding.chapterPageNovelHomeBtn.setOnClickListener {
            onNovelHome()
        }
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
        Thread {
            // 加载小说章节信息列表
            chapterInfoList.addAll(novelParser.loadNovel(novelInfo.novelUrl))
            runOnUiThread {
                chapterListAdapter.notifyItemRangeChanged(0, chapterInfoList.size)
                chapterContentAdapter.notifyItemRangeChanged(0, chapterInfoList.size)
            }
        }.start()
    }

    override fun onNextChapter() {
        if (currentChapterIndex < chapterInfoList.size) {
            currentChapterIndex++;
            viewBinding.chapterContentRecycler.scrollToPosition(currentChapterIndex)
        } else {
            Toast.makeText(this, "已经是最后一章!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLastChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--;
            viewBinding.chapterContentRecycler.scrollToPosition(currentChapterIndex)
        } else {
            Toast.makeText(this, "已经是第一章!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSettingView() {
        Log.d("Anvei", viewBinding.chapterSetting.visibility.toString())
        if (viewBinding.chapterSetting.visibility == View.GONE) {
            viewBinding.chapterSetting.visibility = View.VISIBLE
        } else {
            viewBinding.chapterSetting.visibility = View.GONE
        }
    }

    override fun onNovelHome() {
        NovelHomeActivity.startActivity(this, novelInfo)
    }

    override fun onChapterListView(flag: Boolean) {
        viewBinding.readPageDrawer.openDrawer(GravityCompat.START)
        onSettingView()
    }

    override fun onCurrentChapter(currentIndex: Int) {
        currentChapterIndex = currentIndex
        viewBinding.chapterContentRecycler.scrollToPosition(currentChapterIndex)
    }
}