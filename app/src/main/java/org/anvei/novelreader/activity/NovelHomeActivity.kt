package org.anvei.novelreader.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import org.anvei.novelreader.R
import org.anvei.novelreader.disk.BookShelfHelper
import org.anvei.novelreader.model.NovelInfo
import org.anvei.novelreader.novel.NovelParserFactory
import org.anvei.novelreader.novel.WebsiteNovelParser

// 小说主页显示
class NovelHomeActivity : BaseActivity() {

    private lateinit var novelInfo: NovelInfo

    private lateinit var novelParser: WebsiteNovelParser

    private lateinit var database: SQLiteDatabase

    private lateinit var novelTitleView: TextView

    private lateinit var novelAuthorView: TextView

    private lateinit var novelCoverView: ImageView

    private lateinit var likeButton: Button

    private lateinit var readButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_home)
        novelInfo = intent.getSerializableExtra(EXTRA_NOVEL_INFO) as NovelInfo
        novelParser = NovelParserFactory.getParser(novelInfo.identifier)

        database = BookShelfHelper(this, DATABASE_NAME, null, 1).writableDatabase

        initWidget()
    }

    @SuppressLint("Range")
    private fun initWidget() {
        likeButton = findViewById(R.id.novelHomeLike)
        readButton = findViewById(R.id.novelHomeRead)
        novelTitleView = findViewById(R.id.novelHomeTitle)
        novelAuthorView = findViewById(R.id.novelHomeAuthor)
        novelCoverView = findViewById(R.id.novelHomeCover)

        novelTitleView.text = novelInfo.novel.name
        novelAuthorView.text = novelInfo.novel.author


        if (novelInfo.picUrl != null) {
            Glide.with(this).load(novelInfo.picUrl).into(novelCoverView)
        }


        likeButton.setOnClickListener {
            val cursor = database.query(TABLE_BOOK_SHELF_ITEM, null, "website = ? and novel = ?",
                arrayOf(novelInfo.identifier.name, novelInfo.novel.name), null, null, null)
            if (!cursor.moveToNext()) {
                val contentValues = ContentValues()
                contentValues.put("website", novelInfo.identifier.name)
                contentValues.put("novel", novelInfo.novel.name)
                contentValues.put("author", novelInfo.novel.author)
                contentValues.put("url", novelInfo.url)
                contentValues.put("pic_url", novelInfo.picUrl)
                database.insert(TABLE_BOOK_SHELF_ITEM, "null", contentValues)
            } else {
                Toast.makeText(this, "已经在书架中！", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }

        readButton.setOnClickListener {
            ReadPageActivity.startActivity(this, novelInfo)
        }
    }

    companion object {
        fun startActivity(context: Context, startActivityInfo: NovelInfo) {
            val intent = Intent(context, NovelHomeActivity::class.java)
            intent.putExtra(EXTRA_NOVEL_INFO, startActivityInfo)
            context.startActivity(intent)
        }
    }
}