package org.anvei.novelreader.activity

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.MainPagerAdapter

class MainActivity : BaseActivity() {

    private lateinit var viewPager: ViewPager

    private lateinit var radioGroup: RadioGroup

    private lateinit var bookShelfTab: RadioButton
    private lateinit var suggestTab: RadioButton
    private lateinit var settingTab: RadioButton

    private lateinit var suggestSearch: TextView

    private lateinit var mainBookShelf: View
    private lateinit var mainSuggest: View
    private lateinit var mainSetting: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidget()

    }

    private fun initWidget() {
        viewPager = findViewById(R.id.mainViewPager)
        mainBookShelf = layoutInflater.inflate(R.layout.main_book_shelf, null)
        mainSuggest = layoutInflater.inflate(R.layout.main_suggest, null)
        mainSetting = layoutInflater.inflate(R.layout.main_setting, null)

        val mainViews: List<View> = listOf(mainBookShelf, mainSuggest, mainSetting)

        viewPager.adapter = MainPagerAdapter(mainViews)

        radioGroup = findViewById(R.id.mainRadio)
        bookShelfTab = findViewById(R.id.mainBookShelf)
        suggestTab = findViewById(R.id.mainSuggest)
        settingTab = findViewById(R.id.mainSetting)

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

        // 给按钮设置点击事件监听
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mainBookShelf -> viewPager.currentItem = 0
                R.id.mainSuggest -> viewPager.currentItem = 1
                R.id.mainSetting -> viewPager.currentItem = 2
            }
        }

        suggestSearch = mainSuggest.findViewById(R.id.mainSuggestSearch)
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return true
    }
}