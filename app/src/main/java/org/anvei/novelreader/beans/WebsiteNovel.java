package org.anvei.novelreader.beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.anvei.novelreader.novel.website.WebsiteIdentifier;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;

@Entity(tableName = WebsiteNovel.tableName)
public class WebsiteNovel implements Serializable {     // 为了可以被Intent携带，实现了序列化接口

    @Ignore
    public static final String tableName = "WebsiteNovel";

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public WebsiteIdentifier website;           // 小说网站标识符、可能会因为用户换源而更改
                                                // 需要根据该标识符获取相应的WebsiteNovelParser
    public String author;

    @ColumnInfo(name = "novel_name")
    public String novelName;

    @ColumnInfo(name = "novel_url")
    public String novelUrl;

    @ColumnInfo(name = "novel_cache")
    public File novelCache;                     // 整本小说的缓存、如果是单章节缓存、novelCache为null

    @ColumnInfo(name = "cover_url")
    public String coverUrl;

    @ColumnInfo(name = "cover_cache")
    public File coverCache;                     // 小说封面缓存

    public String intro;                        // 小说简介

    @ColumnInfo(name = "last_chapter_name")
    public String lastChapterName;              // 最新章节的名称

    @ColumnInfo(name = "last_read_chapter_index")
    public int lastReadChapterIndex;            // 最后阅读该小说的章节序号

    @ColumnInfo(name = "last_read_position")
    public int lastReadPosition;                // 最后阅读该小说具体位置：一般保存的是最后阅读小说的页码

    @ColumnInfo(name = "last_read_time")
    public Date lastReadTime;                   // 最后阅读时间

    @ColumnInfo(name = "last_update_time")
    public Date lastUpdateTime;                 // 最后更新时间

}
