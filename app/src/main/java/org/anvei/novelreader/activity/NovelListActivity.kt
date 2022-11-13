package org.anvei.novelreader.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import org.anvei.novelreader.adapter.SearchItemAdapter
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.databinding.ActivityNovelListBinding
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteNovelParser

class NovelListActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityNovelListBinding

    private lateinit var novelParsers: List<WebsiteNovelParser>

    private lateinit var novelInfoList: ArrayList<WebsiteNovelInfo>
    private lateinit var searchItemAdapter: SearchItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNovelListBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        novelParsers = NovelParserFactory.websiteNovelParsers
        initComponent()
        loadNovelInfoList(intent.getCharSequenceExtra(EXTRA_SEARCH_KEYWORD))
    }

    private fun initComponent() {
        novelInfoList = ArrayList()
        searchItemAdapter = SearchItemAdapter(novelInfoList, this)
        viewBinding.srpRecycler.adapter = searchItemAdapter
        viewBinding.srpRecycler.layoutManager = LinearLayoutManager(this)

        viewBinding.srpSearchBar.setText(intent.getCharSequenceExtra(EXTRA_SEARCH_KEYWORD))
        viewBinding.srpSearchBar.setBtnOnClickListener {
            loadNovelInfoList(it)
        }
    }

    private fun loadNovelInfoList(keyWord: CharSequence?) {
        if (TextUtils.isEmpty(keyWord) || keyWord.toString().trim() == "") {
            Toast.makeText(baseContext, "不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        Thread {
            val tempSize = novelInfoList.size
            novelInfoList.clear()               // 清空数据之后需要Adapter同步数据
            runOnUiThread {
                searchItemAdapter.notifyItemRangeRemoved(0, tempSize)
            }
            novelParsers.forEach {
                val tempList = it.search(keyWord.toString())
                val start = novelInfoList.size
                novelInfoList.addAll(tempList)
                runOnUiThread {
                    searchItemAdapter.notifyItemRangeChanged(start, novelInfoList.size)
                }
            }
        }.start()
    }
}