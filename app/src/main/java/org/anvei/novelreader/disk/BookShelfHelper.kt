package org.anvei.novelreader.disk

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import org.anvei.novelreader.activity.BaseActivity
import org.anvei.novelreader.beans.WebsiteNovelInfo
import org.anvei.novelreader.novel.WebsiteIdentifier.Companion.getIdentifier
import java.io.File

/**
 * 存储书架上的小说基本信息的表
 */
class BookShelfHelper(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_HISTORY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists BookShelfItem")
        onCreate(db)
    }

    companion object {
        const val TABLE_NAME = "BookShelfItem"
        const val COL_WEBSITE = "website"
        const val COL_NOVEL_NAME = "novel"
        const val COL_AUTHOR = "author"
        const val COL_NOVEL_URL = "url"
        const val COL_NOVEL_PATH = "path"
        const val COL_COVER_URL = "pic_url"
        const val COL_COVER_PATH = "pic_path"
        const val COL_NEWEST_CHAPTER = "newest_chapter"
        const val COL_NEWEST_INDEX = "newest_index"

        // 建表SQL语句
        private const val CREATE_HISTORY = ("create table BookShelfItem("
                + "id integer primary key autoincrement, "
                + "website text, " // 网站标识
                + "novel text, " // 小说名称
                + "author text, " // 作者
                + "url text, " // 小说链接
                + "path text, " // 本地路径
                + "pic_url text, " // 封面链接
                + "pic_path text,  " // 封面路径
                + "newest_chapter text, " // 最新章节名称
                + "newest_index integer)") // 最新章节序号

        /**
         * 从给定的Cursor对象中查询出NovelInfo列表
         * 主要用来从表中查询书架中的小说信息
         */
        fun queryAllBook(context: Context): List<WebsiteNovelInfo> {
            val list: MutableList<WebsiteNovelInfo> = ArrayList()
            val database =
                BookShelfHelper(context, BaseActivity.DATABASE_NAME, null, 1).readableDatabase
            val cursor = database.query(TABLE_NAME, null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    // 从cursor中读取信息
                    val website = cursor.getString(cursor.getColumnIndexOrThrow(COL_WEBSITE))
                    val novelName = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOVEL_NAME))
                    val author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR))
                    val novelUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOVEL_URL))
                    val novelPath = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOVEL_PATH))
                    val coverUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_COVER_URL))
                    val coverPath = cursor.getString(cursor.getColumnIndexOrThrow(COL_COVER_PATH))
                    val newestChapter = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_NEWEST_CHAPTER
                        )
                    )
                    val newestIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COL_NEWEST_INDEX))
                    val identifier = getIdentifier(website)
                    if (identifier != null) {
                        val novelInfo =
                            WebsiteNovelInfo(identifier, novelName, WebsiteNovelInfo.Status.UNKNOWN)
                        novelInfo.novelUrl = novelUrl
                        if (novelPath != null && File(novelPath).exists()) {
                            // TODO
                        }
                        if (coverUrl != null) {
                            novelInfo.coverUrl = coverUrl
                        }
                        if (coverPath != null && File(coverPath).exists()) {
                            // TODO
                        }
                        if (newestChapter != null) {
                            // TODO
                        }
                        list.add(novelInfo)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            return list
        }
    }
}