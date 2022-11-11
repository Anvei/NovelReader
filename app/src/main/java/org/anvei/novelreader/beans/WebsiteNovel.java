package org.anvei.novelreader.beans;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.anvei.novelreader.novel.WebsiteIdentifier;

import java.sql.Date;

@Entity(tableName = WebsiteNovel.tableName)
public class WebsiteNovel {

    public WebsiteNovel() {}

    @Ignore
    public WebsiteNovel(WebsiteNovelInfo novelInfo) {
        this.website = novelInfo.getIdentifier().name();
        this.author = novelInfo.getAuthor();
        this.novelName = novelInfo.getNovelName();
        this.novelUrl = novelInfo.getNovelUrl();
        this.coverUrl = novelInfo.getCoverUrl();
        this.intro = novelInfo.getIntro();
    }

    @Ignore
    public static final String tableName = "WebsiteNovel";

    @Ignore
    public @Nullable WebsiteNovelInfo getWebsiteNovelInfo() {
        WebsiteIdentifier identifier = WebsiteIdentifier.getIdentifier(website);
        if (identifier == null) return null;
        WebsiteNovelInfo websiteNovelInfo = new WebsiteNovelInfo(identifier, novelName);
        websiteNovelInfo.setAuthor(author);
        websiteNovelInfo.setNovelUrl(novelUrl);
        websiteNovelInfo.setCoverUrl(coverUrl);
        websiteNovelInfo.setIntro(intro);
        return websiteNovelInfo;
    }

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String website;

    public String author;

    @ColumnInfo(name = "novel_name")
    public String novelName;

    @ColumnInfo(name = "novel_url")
    public String novelUrl;

    public String novelCache;

    public String coverUrl;

    public String coverCache;

    public String intro;

    public int lastReadChapter;

    public int lastReadPosition;

    public Date lastReadTime;

    public Date lastUpdateTime;
}
