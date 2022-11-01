package org.anvei.novelreader.activity

import android.os.Bundle
import org.anvei.novelreader.R
import org.anvei.novelreader.novel.BiqumuParser

class ReadPageActivity : BaseActivity() {

    private val parser = BiqumuParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_page)
    }

}