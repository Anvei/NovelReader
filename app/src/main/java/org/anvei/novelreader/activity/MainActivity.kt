package org.anvei.novelreader.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.viewpager.widget.ViewPager
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.MainPagerAdapter

class MainActivity : BaseActivity() {

    private lateinit var viewPager: ViewPager

    private lateinit var radioGroup: RadioGroup

    private lateinit var bookShelfTab: RadioButton
    private lateinit var suggestTab: RadioButton
    private lateinit var settingTab: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWidget()

    }

    private fun initWidget() {
        viewPager = findViewById(R.id.mainViewPager)
        val mainViews: List<View> = listOf(layoutInflater.inflate(R.layout.main_book_shelf, null),
            layoutInflater.inflate(R.layout.main_suggest, null),
            layoutInflater.inflate(R.layout.main_setting, null))
        viewPager.adapter = MainPagerAdapter(mainViews)

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
                        suggestTab.isChecked = true
                        settingTab.isChecked = false
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
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return true
    }
}