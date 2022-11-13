package org.anvei.novelreader.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.BookShelfAdapter
import org.anvei.novelreader.adapter.MainPagerAdapter
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.databinding.ActivityMainBinding
import org.anvei.novelreader.interfaces.view.IBookShelfView
import org.anvei.novelreader.ui.view.SearchBar

open class MainActivity : BaseActivity(), IBookShelfView {

    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var bookShelfView: View
    private lateinit var findView: View
    private lateinit var settingView: View

    private lateinit var mpfSearchBar: SearchBar        // 发现界面顶部的搜索栏

    private lateinit var mpbSearchBtn: ImageButton
    private lateinit var mpbSetting: ImageButton
    private lateinit var mpbEmptyView: View

    // 书架相关变量
    private lateinit var bookShelfItemRecyclerView: RecyclerView
    private lateinit var bookShelfItemAdapter: BookShelfAdapter
    private val bookShelfItemList = ArrayList<WebsiteNovelInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initComponent()
    }

    // 在显示在前台可见时重新加载数据
    override fun onStart() {
        super.onStart()
        initBookShelfItemList()
    }

    // 一离开前台可见就清空数据
    override fun onStop() {
        super.onStop()
        bookShelfItemList.clear()
    }

    /**
     * 对书架上的小说进行初始化
     */
    private fun initBookShelfItemList() {
        Thread {
            val list = appDatabase.websiteNovelDao.queryAllNovel()
            list.forEach {
                bookShelfItemList.add(it.websiteNovelInfo!!)
            }
            runOnUiThread {
                bookShelfItemAdapter.notifyItemRangeChanged(0, bookShelfItemList.size)
                notifyBookShelfChanged()
            }
        }.start()
    }

    /**
     * 初始化控件
     */
    @SuppressLint("InflateParams")
    private fun initComponent() {
        // 初始化主页ViewPager
        bookShelfView = layoutInflater.inflate(R.layout.main_book_shelf, null)
        findView = layoutInflater.inflate(R.layout.main_find, null)
        settingView = layoutInflater.inflate(R.layout.main_setting, null)
        val pagerViews: List<View> = listOf(bookShelfView, findView, settingView)
        viewBinding.mpViewPager.adapter = MainPagerAdapter(pagerViews)
        viewBinding.mpViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            // 根据ViewPager的滑动更新RadioGroup的状态
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        viewBinding.mpRadioBookShelf.isChecked = true
                        viewBinding.mpRadioFind.isChecked = false
                        viewBinding.mpRadioSetting.isChecked = false
                    }
                    1 -> {
                        viewBinding.mpRadioBookShelf.isChecked = false
                        viewBinding.mpRadioFind.isChecked = true
                        viewBinding.mpRadioSetting.isChecked = false
                    }
                    2 -> {
                        viewBinding.mpRadioBookShelf.isChecked = false
                        viewBinding.mpRadioFind.isChecked = false
                        viewBinding.mpRadioSetting.isChecked = true
                    }
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
        // 给RadioButton按钮设置点击事件监听
        viewBinding.mpRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mp_radio_book_shelf -> viewBinding.mpViewPager.currentItem = 0
                R.id.mp_radio_find -> viewBinding.mpViewPager.currentItem = 1
                R.id.mp_radio_setting -> viewBinding.mpViewPager.currentItem = 2
            }
        }
        initBookShelfView()
        initFindView()
    }

    private fun initBookShelfView() {
        mpbEmptyView = bookShelfView.findViewById(R.id.mpb_empty_view)
        mpbSetting = bookShelfView.findViewById(R.id.mpb_setting)
        mpbSearchBtn = bookShelfView.findViewById(R.id.mpb_search)
        mpbSearchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        // 对书架进行初始化
        bookShelfItemRecyclerView = bookShelfView.findViewById(R.id.bookShelf)
        bookShelfItemAdapter = BookShelfAdapter(bookShelfItemList, this)
        bookShelfItemRecyclerView.adapter = bookShelfItemAdapter
        bookShelfItemRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initFindView() {
        // 发现界面顶部的搜索栏控件初始化
        mpfSearchBar = findView.findViewById(R.id.mpf_search_bar)
        mpfSearchBar.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    // 从书架上删除指定下标小说
    override fun deleteNovel(index: Int) {
        bookShelfItemList.removeAt(index)
        // 同步Recycler内部和外部数据
        bookShelfItemAdapter.notifyItemRemoved(index)
        bookShelfItemAdapter.notifyItemRangeChanged(index, bookShelfItemList.size - index)
        notifyBookShelfChanged()
    }

    private fun notifyBookShelfChanged() {
        if (bookShelfItemList.size == 0) {
            mpbEmptyView.visibility = View.VISIBLE
        } else {
            mpbEmptyView.visibility = View.GONE
        }
    }
}