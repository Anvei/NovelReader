package org.anvei.novelreader.model;

import androidx.annotation.NonNull;

import org.anvei.novelreader.novel.WebsiteIdentifier;

import java.io.File;

public class NovelInfo {

    private final Novel novel;

    public final WebsiteIdentifier identifier;

    private String introduction;        // 简介

    private String url;                 // 小说地址

    private String picUrl;              // 小说封面地址

    private File cachePic;              // 缓存的小说封面

    public NovelInfo(@NonNull Novel novel, @NonNull WebsiteIdentifier identifier) {
        this.novel = novel;
        this.identifier = identifier;
    }

    public Novel getNovel() {
        return novel;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getUrl() {
        return url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public File getCachePic() {
        return cachePic;
    }

    public NovelInfo setIntroduction(String introduction) {
        this.introduction = introduction;
        return this;
    }

    public NovelInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public NovelInfo setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public NovelInfo setCachePic(File cachePic) {
        this.cachePic = cachePic;
        return this;
    }
}
