package org.anvei.novelreader.model;

import java.io.File;

public class NovelInfo {

    private final Novel novel;

    private String introduction;        // 简介

    private String url;                 // 小说地址

    private String picUrl;              // 小说封面地址

    private File cachePic;              // 缓存的小说封面

    public NovelInfo(Novel novel) {
        this.novel = novel;
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
