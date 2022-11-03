package org.anvei.novelreader.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.ChapterItemAdapter
import org.anvei.novelreader.model.ChapterInfo
import org.anvei.novelreader.model.WebsiteIdentifier
import org.anvei.novelreader.novel.parser.BiqumuParser
import org.anvei.novelreader.novel.WebsiteNovelParser
import org.anvei.novelreader.novel.parser.SfacgParser
import org.anvei.novelreader.novel.parser.W147xsParser

class ReadPageActivity : BaseActivity() {

    private lateinit var novelParser: WebsiteNovelParser        // 网络小说解析器

    private lateinit var readPageDrawer: DrawerLayout

    private lateinit var chapterContent: TextView               // 章节显示的内容

    private lateinit var chapterNavigationView: NavigationView  // NavigationView

    private lateinit var navigationHeaderView: View         // NavigationView设置的headerView

    private lateinit var chapterListView: RecyclerView      // 章节列表

    private lateinit var novelTitle: TextView               // 章节列表之上显示的小说名

    private lateinit var chapterListAdapter: ChapterItemAdapter

    private val chapterList = ArrayList<ChapterInfo>()

    private var currentIndex = 0

    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_page)

        val name = intent.getStringExtra(EXTRA_NOVEL_HOME_NAME)
        val author = intent.getStringExtra(EXTRA_NOVEL_HOME_AUTHOR)
        val url = intent.getStringExtra(EXTRA_NOVEL_HOME_URL)

        // 根据小说网站标识符初始化相应的网络小说解析器
        when (intent.getSerializableExtra(EXTRA_NOVEL_HOME_WEBSITE)) {
            WebsiteIdentifier.SFACG -> {
                novelParser = SfacgParser()
            }
            WebsiteIdentifier.BIQUMU -> {
                novelParser = BiqumuParser()
            }
            WebsiteIdentifier.W147XS -> {
                novelParser = W147xsParser()
            }
        }
        initView()
        novelTitle.text = name

        Thread {
            chapterList.addAll(novelParser.loadNovel(url))
            runOnUiThread {
                chapterListAdapter.notifyItemRangeChanged(0, chapterList.size)
            }
            if (chapterList.size != 0) {
                val firstChapter = novelParser.loadChapter(chapterList[0])
                runOnUiThread {
                    chapterContent.text = firstChapter.content
                }
            }
        }.start()

    }

    private fun initView() {
        readPageDrawer = findViewById(R.id.readPageDrawer)

        chapterContent = findViewById(R.id.readContent)
        chapterNavigationView = findViewById(R.id.readNavigation)
        navigationHeaderView = chapterNavigationView.getHeaderView(0)

        chapterListView = navigationHeaderView.findViewById(R.id.navigationHeaderRecycler)
        novelTitle = navigationHeaderView.findViewById(R.id.navigationHeaderTitle)

        chapterListAdapter = ChapterItemAdapter(chapterList, this, novelParser)
        chapterListView.adapter = chapterListAdapter
        chapterListView.layoutManager = LinearLayoutManager(this)

        floatingButton = findViewById(R.id.floatingButton)
        floatingButton.setOnClickListener{
            // TODO: 滑动到当前章节位置
            readPageDrawer.openDrawer(GravityCompat.START)
        }
    }



}