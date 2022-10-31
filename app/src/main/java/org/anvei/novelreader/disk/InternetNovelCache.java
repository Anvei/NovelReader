package org.anvei.novelreader.disk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 管理小说章节缓存
 */
public class InternetNovelCache extends SQLiteOpenHelper {

    private final String CREATE_WEB_CACHE = "create table WebCache("
            + "id integer primary key autoincrement ,"
            + "website text, "
            + "novel text, "
            + "author text, "
            + "url text, "          // 小说链接
            + "path text)";         // 该小说缓存所在的的文件夹

    public InternetNovelCache(@Nullable Context context, @Nullable String name,
                              @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEB_CACHE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists WebCache");
        onCreate(db);
    }
}
