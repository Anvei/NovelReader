package org.anvei.novelreader.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import org.anvei.novelreader.R
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteNovelParser

// 小说主页显示
class NovelHomeActivity : BaseActivity() {

    private lateinit var novelInfo: WebsiteNovelInfo

    private lateinit var novelParser: WebsiteNovelParser

    private lateinit var novelTitleView: TextView

    private lateinit var novelAuthorView: TextView

    private lateinit var novelCoverView: ImageView

    private lateinit var likeButton: Button

    private lateinit var readButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_home)
        novelInfo = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as WebsiteNovelInfo
        novelParser = NovelParserFactory.getParser(novelInfo.identifier)

        initWidget()
    }

    @SuppressLint("Range")
    private fun initWidget() {
        likeButton = findViewById(R.id.novelHomeLike)
        readButton = findViewById(R.id.novelHomeRead)
        novelTitleView = findViewById(R.id.novelHomeTitle)
        novelAuthorView = findViewById(R.id.novelHomeAuthor)
        novelCoverView = findViewById(R.id.novelHomeCover)

        novelTitleView.text = novelInfo.novelName
        novelAuthorView.text = novelInfo.author

        if (novelInfo.coverUrl != null) {
            Glide.with(this).load(novelInfo.coverUrl).into(novelCoverView)
        }

        likeButton.setOnClickListener {
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

        readButton.setOnClickListener {
            ReadPageActivity.startActivity(this, novelInfo)
        }
    }

    companion object {
        fun startActivity(context: Context, startActivityInfo: WebsiteNovelInfo) {
            val intent = Intent(context, NovelHomeActivity::class.java)
            intent.putExtra(EXTRA_NOVEL_INFO, startActivityInfo)
            context.startActivity(intent)
        }
    }
}