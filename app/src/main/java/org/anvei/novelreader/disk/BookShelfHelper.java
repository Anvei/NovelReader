package org.anvei.novelreader.disk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 存储书架上的小说基本信息的表
 */
public class BookShelfHelper extends SQLiteOpenHelper {

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
}
