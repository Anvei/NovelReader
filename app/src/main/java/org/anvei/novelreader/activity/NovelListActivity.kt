package org.anvei.novelreader.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.SearchItemAdapter
import org.anvei.novelreader.model.NovelInfo
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteNovelParser

class NovelListActivity : BaseActivity() {

    private lateinit var novelRecycler: RecyclerView

    private lateinit var novelParsers: List<WebsiteNovelParser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_list)

        novelRecycler = findViewById(R.id.searchResultList)

        val keyword = intent.getStringExtra(EXTRA_SEARCH_KEYWORD)
        novelParsers = NovelParserFactory.getWebsiteNovelParsers()

        val novelList = ArrayList<NovelInfo>()
        val adapter = SearchItemAdapter(novelList, this)

        novelRecycler.adapter = adapter
        novelRecycler.layoutManager = LinearLayoutManager(this)

        Thread {
            novelParsers.forEach {
                val tempList = it.search(keyword)
                val start = novelList.size
                novelList.addAll(tempList)
                runOnUiThread {
                    adapter.notifyItemRangeChanged(start, novelList.size)
                }
            }
        }.start()
    }
}