package org.anvei.novelreader.activity

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.BookShelfAdapter
import org.anvei.novelreader.adapter.MainPagerAdapter
import org.anvei.novelreader.disk.BookShelfHelper
import org.anvei.novelreader.beans.WebsiteNovelInfo

class MainActivity : BaseActivity() {

    private lateinit var dataBase: SQLiteDatabase       // 数据库

    private lateinit var viewPager: ViewPager           // 主页的ViewPager
    private lateinit var mainBookShelf: View
    private lateinit var mainSuggest: View
    private lateinit var mainSetting: View

    private lateinit var radioGroup: RadioGroup         // 主页下的RadioGroup
    private lateinit var bookShelfTab: RadioButton
    private lateinit var suggestTab: RadioButton
    private lateinit var settingTab: RadioButton

    private lateinit var suggestSearch: TextView        // 发现界面顶部的搜索栏

    // 书架相关变量
    private lateinit var bookShelfItemRecyclerView: RecyclerView
    private lateinit var bookShelfItemAdapter: BookShelfAdapter
    private val bookShelfItemList = ArrayList<WebsiteNovelInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActivity()
    }

    private fun initActivity() {
        initDatabase()
        initBookShelfItemList()
        initComponent()
    }

    /**
     * 对书架上的小说进行初始化
     */
    private fun initBookShelfItemList() {
        bookShelfItemList.addAll(BookShelfHelper.queryAllBook(this))
    }

    /**
     * 初始化数据库
     */
    private fun initDatabase() {
        dataBase = BookShelfHelper(this, DATABASE_NAME, null, 1).writableDatabase
    }

    /**
     * 初始化控件
     */
    private fun initComponent() {
        // 初始化主页ViewPager
        viewPager = findViewById(R.id.mainViewPager)
        mainBookShelf = layoutInflater.inflate(R.layout.main_book_shelf, null)
        mainSuggest = layoutInflater.inflate(R.layout.main_find, null)
        mainSetting = layoutInflater.inflate(R.layout.main_setting, null)
        val mainViews: List<View> = listOf(mainBookShelf, mainSuggest, mainSetting)
        viewPager.adapter = MainPagerAdapter(mainViews)

        // 初始化RadioGroup控件
        radioGroup = findViewById(R.id.mainRadio)
        bookShelfTab = findViewById(R.id.mainBookShelf)
        suggestTab = findViewById(R.id.mainSuggest)
        settingTab = findViewById(R.id.mainSetting)

        // 为ViewPager设置页面滑动监听
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bookShelfTab.isChecked = true
                        suggestTab.isChecked = false
                        settingTab.isChecked = false
                    }
                    1 -> {
                        bookShelfTab.isChecked = false
                        suggestTab.isChecked = true
                        settingTab.isChecked = false
                    }
                    2 -> {
                        bookShelfTab.isChecked = false
                        suggestTab.isChecked = false
                        settingTab.isChecked = true
                    }
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        // 给RadioButton按钮设置点击事件监听
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mainBookShelf -> viewPager.currentItem = 0
                R.id.mainSuggest -> viewPager.currentItem = 1
                R.id.mainSetting -> viewPager.currentItem = 2
            }
        }

        // 对书架进行初始化
        bookShelfItemRecyclerView = mainBookShelf.findViewById(R.id.bookShelf)
        bookShelfItemAdapter = BookShelfAdapter(bookShelfItemList, this)
        bookShelfItemRecyclerView.adapter = bookShelfItemAdapter
        bookShelfItemRecyclerView.layoutManager = LinearLayoutManager(this)

        // 发现界面顶部的搜索栏控件初始化
        suggestSearch = mainSuggest.findViewById(R.id.mainFindTextView)
        val spannableString = SpannableStringBuilder()
        spannableString.append("搜索 关键字")
        val imageSpan = ImageSpan(this, R.drawable.search)
        //将'搜索'用图片替代
        spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        suggestSearch.text = spannableString
        suggestSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}