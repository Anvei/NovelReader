package org.anvei.novelreader.activity

import android.os.Bundle
import org.anvei.novelreader.R

class NovelListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_list)

        val keyword = intent.getStringExtra(EXTRA_NOVEL_URL)

    }
}