package org.anvei.novelreader.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.ChapterContentAdapter
import org.anvei.novelreader.adapter.ChapterItemAdapter
import org.anvei.novelreader.model.ChapterInfo
import org.anvei.novelreader.model.NovelInfo
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteNovelParser

class ReadPageActivity : BaseActivity() {

    // 小说基本信息
    private lateinit var novelParser: WebsiteNovelParser
    private lateinit var novelInfo: NovelInfo
    private val chapterInfoList = ArrayList<ChapterInfo>()

    // 主体视图
    private lateinit var readPageDrawer: DrawerLayout
    private lateinit var chapterNavigationView: NavigationView   // NavigationView
    private lateinit var navigationHeaderView: View              // NavigationView设置的headerView

    private lateinit var chapterSettingView: LinearLayout        // 点击章节页面之后出现的设置面板

    private lateinit var chapterListView: RecyclerView           // 章节列表
    private lateinit var novelTitle: TextView                    // 章节列表之上显示的小说名
    private lateinit var chapterContentRecycler: RecyclerView    // 章节内容RecyclerView

    private lateinit var chapterListAdapter: ChapterItemAdapter
    private lateinit var chapterContentAdapter: ChapterContentAdapter

    private var currentIndex = 0                // 当前章节号

    /**
     * 初始化小说基本信息、章节信息、小说解析器
     */
    private fun initNovelConfig() {
        novelInfo = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as NovelInfo
        // 根据小说网站标识符初始化相应的网络小说解析器
        novelParser = NovelParserFactory.getParser(novelInfo.identifier)
    }

    /**
     * 初始化界面中的主题视图
     */
    private fun initBasicView() {
        readPageDrawer = findViewById(R.id.readPageDrawer)
        chapterNavigationView = findViewById(R.id.readNavigation)
        navigationHeaderView = chapterNavigationView.getHeaderView(0)
    }

    /**
     * 初始化小说阅读界面下的设置面板视图
     */
    private fun initSettingView() {
        chapterSettingView = findViewById(R.id.chapterSettingView)
        findViewById<Button>(R.id.chapterListDisplay).setOnClickListener {
            readPageDrawer.openDrawer(GravityCompat.START)
            chapterSettingView.visibility = View.GONE
        }
        findViewById<Button>(R.id.chapterPageNovelHome).setOnClickListener {
            NovelHomeActivity.startActivity(this, novelInfo)
        }
        // 配置上一章、下一章按钮的点击事件
        findViewById<ImageButton>(R.id.lastChapter).setOnClickListener {
            if (currentIndex > 1) {
                setCurrentChapter(chapterInfoList[currentIndex - 2])
            } else {
                Toast.makeText(this, "当前已经是第一章!", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<ImageButton>(R.id.nextChapter).setOnClickListener {
            if (currentIndex < chapterInfoList.size) {
                setCurrentChapter(chapterInfoList[currentIndex])
            } else {
                Toast.makeText(this, "当前已经是最后一章!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 初始化小说章节列表视图
     */
    private fun initChapterListView() {
        chapterListView = navigationHeaderView.findViewById(R.id.navigationHeaderRecycler)
        novelTitle = navigationHeaderView.findViewById(R.id.navigationHeaderTitle)
        chapterListAdapter = ChapterItemAdapter(chapterInfoList, this)
        chapterListView.adapter = chapterListAdapter
        chapterListView.layoutManager = LinearLayoutManager(this)
        novelTitle.text = novelInfo.novel.name
    }

    /**
     * 初始化小说章节阅读界面视图
     */
    private fun initChapterContentView() {
        chapterContentRecycler = findViewById(R.id.chapterContentRecycler)
        chapterContentAdapter = ChapterContentAdapter(chapterInfoList, this, novelParser)
        chapterContentRecycler.adapter = chapterContentAdapter
        chapterContentRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun init() {
        initNovelConfig()
        initBasicView()
        initSettingView()
        initChapterListView()
        initChapterContentView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_page)
        init()
        Thread {
            // 加载小说章节信息列表
            chapterInfoList.addAll(novelParser.loadNovel(novelInfo.url))
            runOnUiThread {
                chapterListAdapter.notifyItemRangeChanged(0, chapterInfoList.size)
                chapterContentAdapter.notifyItemRangeChanged(0, chapterInfoList.size)
            }
        }.start()
    }

    /**
     * 提供给ChapterListAdapter的接口函数
     */
    fun setCurrentChapter(chapterInfo: ChapterInfo) {
        chapterContentRecycler.scrollToPosition(chapterInfo.index)
    }

    fun setCurrentIndex(index: Int) {
        currentIndex = index
    }

    fun onSettingViewClick() {
        if (chapterSettingView.visibility == View.GONE) {
            chapterSettingView.visibility = View.VISIBLE
        } else {
            chapterSettingView.visibility = View.GONE
        }
    }

    companion object {
        /**
         * 用来启动ReadPageActivity
         */
        fun startActivity(context: Context, startActivityInfo: NovelInfo) {
            val intent = Intent(context, ReadPageActivity::class.java)
            intent.putExtra(EXTRA_NOVEL_INFO, startActivityInfo)
            context.startActivity(intent)
        }
    }

}