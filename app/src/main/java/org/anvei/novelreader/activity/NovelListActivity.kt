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
import org.anvei.novelreader.ui.view.SearchBar

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
        loadNovelInfoList(intent.getStringExtra(EXTRA_SEARCH_KEYWORD))
    }

    private fun initComponent() {
        novelInfoList = ArrayList()
        searchItemAdapter = SearchItemAdapter(novelInfoList, this)
        viewBinding.srpRecycler.adapter = searchItemAdapter
        viewBinding.srpRecycler.layoutManager = LinearLayoutManager(this)
            
        viewBinding.srpSearchBar.setCallBack(object : SearchBar.CallBack() {
            override fun onClickWithEditable(inputText: String?) {
                super.onClickWithEditable(inputText)
                loadNovelInfoList(inputText)
            }

            override fun onClickWithUnEditable() {
                super.onClickWithUnEditable()
                Toast.makeText(baseContext, "点击", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadNovelInfoList(keyWord: String?) {
        if (TextUtils.isEmpty(keyWord)) {
            Toast.makeText(baseContext, "不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        Thread {
            novelInfoList.clear()
            novelParsers.forEach {
                val tempList = it.search(keyWord)
                val start = novelInfoList.size
                novelInfoList.addAll(tempList)
                runOnUiThread {
                    searchItemAdapter.notifyItemRangeChanged(start, novelInfoList.size)
                }
            }
        }.start()
    }
}