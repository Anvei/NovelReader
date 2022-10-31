package org.anvei.novelreader.disk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class History extends SQLiteOpenHelper {

    private final static String CREATE_HISTORY = "create table History("
            + "id integer primary key autoincrement, "
            + "novel text, "
            + "author text, "
            + "url text, "
            + "path text, "
            + "chapter integer)";

    public History(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists History");
        onCreate(db);
    }
}
