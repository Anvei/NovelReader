package org.anvei.novelreader.disk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.anvei.novelreader.activity.BaseActivity;
import org.anvei.novelreader.model.Chapter;
import org.anvei.novelreader.model.ChapterInfo;
import org.anvei.novelreader.model.Novel;
import org.anvei.novelreader.model.NovelInfo;
import org.anvei.novelreader.novel.WebsiteIdentifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储书架上的小说基本信息的表
 */
public class BookShelfHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "BookShelfItem";

    public static final String COL_WEBSITE = "website";

    public static final String COL_NOVEL_NAME = "novel";

    public static final String COL_AUTHOR = "author";

    public static final String COL_NOVEL_URL = "url";

    public static final String COL_NOVEL_PATH = "path";

    public static final String COL_COVER_URL = "pic_url";

    public static final String COL_COVER_PATH = "pic_path";

    public static final String COL_NEWEST_CHAPTER = "newest_chapter";

    public static final String COL_NEWEST_INDEX = "newest_index";

    // 建表SQL语句
    private final static String CREATE_HISTORY = "create table BookShelfItem("
            + "id integer primary key autoincrement, "
            + "website text, "          // 网站标识
            + "novel text, "            // 小说名称
            + "author text, "           // 作者
            + "url text, "              // 小说链接
            + "path text, "             // 本地路径
            + "pic_url text, "          // 封面链接
            + "pic_path text,  "        // 封面路径
            + "newest_chapter text, "   // 最新章节名称
            + "newest_index integer)";  // 最新章节序号

    public BookShelfHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists BookShelfItem");
        onCreate(db);
    }

    /**
     * 从给定的Cursor对象中查询出NovelInfo列表
     * 主要用来从表中查询书架中的小说信息
     */
    public static List<NovelInfo> queryAllBook(@NonNull Context context) {
        List<NovelInfo> list = new ArrayList<>();
        SQLiteDatabase database = new BookShelfHelper(context, BaseActivity.DATABASE_NAME, null, 1).getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 从cursor中读取信息
                String website = cursor.getString(cursor.getColumnIndexOrThrow(COL_WEBSITE));
                String novelName = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOVEL_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR));
                String novelUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOVEL_URL));
                String novelPath = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOVEL_PATH));
                String coverUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_COVER_URL));
                String coverPath = cursor.getString(cursor.getColumnIndexOrThrow(COL_COVER_PATH));
                String newestChapter = cursor.getString(cursor.getColumnIndexOrThrow(COL_NEWEST_CHAPTER));
                int newestIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COL_NEWEST_INDEX));
                WebsiteIdentifier identifier = WebsiteIdentifier.getIdentifier(website);
                if (identifier != null) {
                    NovelInfo novelInfo = new NovelInfo(new Novel(novelName, author), identifier);
                    novelInfo.setUrl(novelUrl);
                    if (novelPath != null && new File(novelPath).exists()) {
                        novelInfo.setCache(new File(novelPath));
                    }
                    if (coverUrl != null) {
                        novelInfo.setPicUrl(coverUrl);
                    }
                    if (coverPath != null && new File(coverPath).exists()) {
                        novelInfo.setCachePic(new File(coverPath));
                    }
                    if (newestChapter != null) {
                        novelInfo.setNewestChapter(new ChapterInfo(new Chapter(newestChapter), newestIndex));
                    }
                    list.add(novelInfo);
                }
            } while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
