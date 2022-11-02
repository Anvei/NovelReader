package org.anvei.novelreader.activity

import android.os.Bundle
import org.anvei.novelreader.R
import org.anvei.novelreader.novel.BiqumuParser
import org.anvei.novelreader.novel.NovelWebsiteParser
import org.anvei.novelreader.novel.SfacgParser

class ReadPageActivity : BaseActivity() {

    private val parser = BiqumuParser()

    private lateinit var novelParser: NovelWebsiteParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_page)

        val name = intent.getStringExtra(EXTRA_NOVEL_HOME_NAME)
        val author = intent.getStringExtra(EXTRA_NOVEL_HOME_AUTHOR)
        val url = intent.getStringExtra(EXTRA_NOVEL_HOME_URL)

        when (intent.getStringExtra(EXTRA_NOVEL_HOME_WEBSITE)) {
            "SFACG" -> {
                novelParser = SfacgParser()
            }
            "BIQUMU" -> {
                novelParser = BiqumuParser()
            }
        }

        Thread {

        }.start()

    }

}