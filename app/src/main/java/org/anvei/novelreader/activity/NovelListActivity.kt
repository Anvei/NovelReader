package org.anvei.novelreader.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.SearchItemAdapter
import org.anvei.novelreader.model.NovelInfo
import org.anvei.novelreader.novel.BiqumuParser
import org.anvei.novelreader.novel.SfacgParser

class NovelListActivity : BaseActivity() {

    private lateinit var novelRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_list)

        novelRecycler = findViewById(R.id.searchResultList)

        val keyword = intent.getStringExtra(EXTRA_SEARCH_KEYWORD)
        val sfacgParser = SfacgParser()
        val biqumuParser = BiqumuParser()

        val novelList = ArrayList<NovelInfo>()
        val adapter = SearchItemAdapter(novelList, this)

        novelRecycler.adapter = adapter
        novelRecycler.layoutManager = LinearLayoutManager(this)

        Thread {
            val sfacgList = sfacgParser.search(keyword)
            novelList.addAll(sfacgList)
            runOnUiThread {
                adapter.notifyItemRangeChanged(0, sfacgList.size)
            }
            val biqumuList = biqumuParser.search(keyword)
            novelList.addAll(biqumuList)
            runOnUiThread {
                adapter.notifyItemRangeChanged(sfacgList.size, sfacgList.size + biqumuList.size)
            }
            Thread.sleep(20000)
        }.start()
    }
}