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
import org.anvei.novelreader.adapter.ChapterItemAdapter
import org.anvei.novelreader.model.ChapterInfo
import org.anvei.novelreader.model.NovelInfo
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteIdentifier
import org.anvei.novelreader.novel.WebsiteNovelParser

class ReadPageActivity : BaseActivity() {

    private lateinit var novelParser: WebsiteNovelParser        // 网络小说解析器

    private lateinit var novelInfo: NovelInfo

    private lateinit var readPageDrawer: DrawerLayout

    private lateinit var chapterTitle: TextView                 // 章节标题

    private lateinit var chapterContent: TextView               // 章节显示的内容

    private lateinit var chapterNavigationView: NavigationView  // NavigationView

    private lateinit var navigationHeaderView: View         // NavigationView设置的headerView

    private lateinit var chapterListView: RecyclerView      // 章节列表

    private lateinit var novelTitle: TextView               // 章节列表之上显示的小说名

    private lateinit var scrollView: ScrollView

    private lateinit var chapterFloatingView: LinearLayout      // 点击章节页面之后出现的浮动面板

    private lateinit var chapterListAdapter: ChapterItemAdapter

    private val chapterList = ArrayList<ChapterInfo>()

    private var currentIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_page)

        novelInfo = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as NovelInfo

        // 根据小说网站标识符初始化相应的网络小说解析器
        novelParser = NovelParserFactory.getParser(novelInfo.identifier)

        initView()
        novelTitle.text = novelInfo.novel.name

        Thread {
            chapterList.addAll(novelParser.loadNovel(novelInfo.url))
            runOnUiThread {
                chapterListAdapter.notifyItemRangeChanged(0, chapterList.size)
            }
            if (chapterList.size != 0) {
                val firstChapter = novelParser.loadChapter(chapterList[0])
                runOnUiThread {
                    chapterTitle.text = firstChapter.name
                    chapterContent.text = firstChapter.content
                }
            }
        }.start()

    }

    private fun setChapter(chapterInfo: ChapterInfo) {
        Thread {
            val chapter = novelParser.loadChapter(chapterInfo)
            runOnUiThread {
                chapterTitle.text = chapter.name
                chapterContent.text = chapter.content
                setCurrentIndex(chapterInfo.index)
                scrollChapterContentToTop()
            }
        }.start()
    }

    fun setCurrentIndex(index: Int) {
        currentIndex = index
    }

    fun getCurrentIndex() = currentIndex

    /**
     * 章节内容定位到顶部，有BUG，需要调用两次才能保证滑动到顶部
     */
    fun scrollChapterContentToTop() {
        scrollView.fullScroll(View.FOCUS_UP)
        scrollView.fullScroll(View.FOCUS_UP)
    }

    private fun initView() {
        readPageDrawer = findViewById(R.id.readPageDrawer)

        scrollView = findViewById(R.id.scrollContent)

        chapterContent = findViewById(R.id.readContent)
        chapterTitle = findViewById(R.id.readChapterTitle)
        chapterNavigationView = findViewById(R.id.readNavigation)
        navigationHeaderView = chapterNavigationView.getHeaderView(0)
        chapterFloatingView = findViewById(R.id.chapterFloatingView)

        findViewById<Button>(R.id.chapterListDisplay).setOnClickListener {
            // TODO: 滑动到当前章节位置
            readPageDrawer.openDrawer(GravityCompat.START)
            chapterFloatingView.visibility = View.GONE
        }
        findViewById<Button>(R.id.chapterPageNovelHome).setOnClickListener {
            NovelHomeActivity.startActivity(this, novelInfo)
        }
        // 配置上一章、下一章按钮的点击事件
        findViewById<ImageButton>(R.id.lastChapter).setOnClickListener {
            if (currentIndex > 1) {
                setChapter(chapterList[currentIndex - 2])
            } else {
                Toast.makeText(this, "当前已经是第一章!", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<ImageButton>(R.id.nextChapter).setOnClickListener {
            if (currentIndex < chapterList.size) {
                setChapter(chapterList[currentIndex])
            } else {
                Toast.makeText(this, "当前已经是最后一章!", Toast.LENGTH_SHORT).show()
            }
        }

        // TODO: 当文字比较短时，无法监听全屏任意位置的点击事件
        chapterContent.setOnClickListener {
            if (chapterFloatingView.visibility == View.GONE) {
                chapterFloatingView.visibility = View.VISIBLE
            } else {
                chapterFloatingView.visibility = View.GONE
            }
        }
        chapterListView = navigationHeaderView.findViewById(R.id.navigationHeaderRecycler)
        novelTitle = navigationHeaderView.findViewById(R.id.navigationHeaderTitle)

        chapterListAdapter = ChapterItemAdapter(chapterList, this, novelParser)
        chapterListView.adapter = chapterListAdapter
        chapterListView.layoutManager = LinearLayoutManager(this)
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