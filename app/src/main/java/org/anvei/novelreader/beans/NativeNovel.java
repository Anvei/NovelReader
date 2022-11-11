package org.anvei.novelreader.beans;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = NativeNovel.TABLE_NAME)
public class NativeNovel {

    @Ignore
    public static final String TABLE_NAME = "NativeNovel";

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String author;

    public String novelName;

    public String novelPath;

    public String lastReadChapter;

    public String lastReadPosition;

    public Date lastReadTime;

    public Date lastUpdateTime;
}
