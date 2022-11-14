package org.anvei.novelreader.beans;

import androidx.annotation.IntRange;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.File;

@Entity(tableName = "WebsiteChapter")
public class WebsiteChapter {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @IntRange(from = 1)
    public int index;                         // 章节序号、从1开始

    @ColumnInfo(name = "chapter_name")
    public String chapterName;

    @ColumnInfo(name = "chapter_url")
    public String chapterUrl;

    @ColumnInfo(name = "chapter_cache")
    public File chapterCache;

    @ColumnInfo(name = "novel_uid")
    public Long novelUid;

    @Ignore
    public String chapterContent;

    @Ignore
    public void saveChapterCache() {
        // TODO: 章节缓存
    }
}
