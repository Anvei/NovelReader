package org.anvei.novelreader.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import org.anvei.novelreader.adapter.SearchItemAdapter
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.databinding.ActivityNovelListBinding
import org.anvei.novelreader.novel.website.NovelParserFactory
import org.anvei.novelreader.novel.website.WebsiteNovelParser

class NovelListActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityNovelListBinding

    private lateinit var novelParsers: List<WebsiteNovelParser>

    private lateinit var searchResultNovelList: ArrayList<WebsiteNovel>
    private lateinit var searchItemAdapter: SearchItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNovelListBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        novelParsers = NovelParserFactory.getWebsiteNovelParsers()
        initComponent()
        loadNovelInfoList(intent.getCharSequenceExtra(EXTRA_SEARCH_KEYWORD))
    }

    private fun initComponent() {
        searchResultNovelList = ArrayList()
        searchItemAdapter = SearchItemAdapter(searchResultNovelList, this)
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
            val tempSize = searchResultNovelList.size
            searchResultNovelList.clear()               // 清空数据之后需要Adapter同步数据
            runOnUiThread {
                searchItemAdapter.notifyItemRangeRemoved(0, tempSize)
            }
            novelParsers.forEach {
                val tempList = it.search(keyWord.toString())
                val start = searchResultNovelList.size
                searchResultNovelList.addAll(tempList)
                runOnUiThread {
                    searchItemAdapter.notifyItemRangeChanged(start, searchResultNovelList.size)
                }
            }
        }.start()
    }
}