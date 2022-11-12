package org.anvei.novelreader.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import org.anvei.novelreader.R
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.databinding.ActivityNovelHomeBinding
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteNovelParser

// 小说主页显示
class NovelHomeActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context, startActivityInfo: WebsiteNovelInfo) {
            val intent = Intent(context, NovelHomeActivity::class.java)
            intent.putExtra(EXTRA_NOVEL_INFO, startActivityInfo)
            context.startActivity(intent)
        }
    }

    private lateinit var viewBinding: ActivityNovelHomeBinding

    private lateinit var novelInfo: WebsiteNovelInfo
    private lateinit var novelParser: WebsiteNovelParser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNovelHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initNovelConfig()
        initComponent()
    }

    private fun initNovelConfig() {
        novelInfo = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as WebsiteNovelInfo
        novelParser = NovelParserFactory.getParser(novelInfo.identifier)
    }

    @SuppressLint("Range")
    private fun initComponent() {
        viewBinding.nhpName.text = novelInfo.novelName
        viewBinding.nhpAuthor.text = novelInfo.author
        if (novelInfo.coverUrl != null) {
            Glide.with(this).load(novelInfo.coverUrl).into(viewBinding.nhpCover)
        }
        viewBinding.nhpLike.setOnClickListener {
            Thread {
                if (appDatabase.websiteNovelDao.queryNovel(novelInfo.identifier.name,
                    novelInfo.author, novelInfo.novelName) == null) {
                    appDatabase.websiteNovelDao.addNovel(WebsiteNovel(novelInfo))
                    runOnUiThread {
                        Toast.makeText(this, "收藏成功!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "已经在书架中!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
        viewBinding.nhpRead.setOnClickListener {
            ReadPageActivity.startActivity(this, novelInfo)
        }
    }
}