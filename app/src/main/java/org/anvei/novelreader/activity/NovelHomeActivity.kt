package org.anvei.novelreader.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.databinding.ActivityNovelHomeBinding
import org.anvei.novelreader.novel.website.NovelParserFactory
import org.anvei.novelreader.novel.website.WebsiteNovelParser
import org.anvei.novelreader.room.repository.NovelRepository

// 小说主页显示
class NovelHomeActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context, websiteNovel: WebsiteNovel) {
            val intent = Intent(context, NovelHomeActivity::class.java)
            intent.putExtra(EXTRA_NOVEL_INFO, websiteNovel)
            context.startActivity(intent)
        }
    }

    private lateinit var viewBinding: ActivityNovelHomeBinding

    private lateinit var currentNovel: WebsiteNovel
    private lateinit var novelParser: WebsiteNovelParser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNovelHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initNovelConfig()
        initComponent()
    }

    private fun initNovelConfig() {
        currentNovel = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as WebsiteNovel
        novelParser = NovelParserFactory.getParser(currentNovel.website)
    }

    @SuppressLint("Range")
    private fun initComponent() {
        viewBinding.nhpName.text = currentNovel.novelName
        viewBinding.nhpAuthor.text = currentNovel.author
        if (currentNovel.intro != null) {
            viewBinding.nhpIntro.text = currentNovel.intro
        }
        if (currentNovel.coverCache != null) {
            Glide.with(this).load(currentNovel.coverCache).into(viewBinding.nhpCover)
        } else if (currentNovel.coverUrl != null) {
            Glide.with(this).load(currentNovel.coverUrl).into(viewBinding.nhpCover)
        } else {
            // TODO: 加载默认封面
        }
        viewBinding.nhpLike.setOnClickListener {
            Thread {
                if (NovelRepository.addNovelToBookShelf(currentNovel)) {
                    runOnUiThread { toast("加入书架成功!") }
                } else {
                    runOnUiThread { toast("已经在书架中!") }
                }
            }.start()
        }
        viewBinding.nhpRead.setOnClickListener {
            ReadPageActivity.startActivity(this, currentNovel)
        }
    }
}